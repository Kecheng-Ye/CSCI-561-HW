package KnowledgeBase;

import FOLExpression.*;
import Utils.RecursionLimiter;

import java.util.*;
import java.util.stream.Stream;

public class KnowledgeBase {
    private final KnowledgeBaseStorage storage;

    public KnowledgeBase() {
        this.storage = new KnowledgeBaseStorage();
    }

    public void tell(final FOLExpressionNode expressionNode) {
        // first convert each sentence to CNF
        final FOLExpressionNode nodeInCNF = FOLExpressionUtil.convertToCNF(expressionNode);
        // then split the conjunction of disjunctions into separate disjunction clauses
        final List<FOLExpressionNode> splitCNF = KnowledgeBaseUtil.splitCNF(nodeInCNF);

        splitCNF.forEach(this.storage::addCNFSentence);
    }

    public boolean ask(final FOLExpressionNode expressionNode) {
        // onTrack used to detect refutation circle
        // A --> B --> A will be detected
        Set<FOLExpressionNode> onTrack = new HashSet<>();
        // hasProved used to record some predicate that has already been proved
        // to prevent reproving same predicate multiple time
        // Note: !!! only those predicates that have all Constant as its arguments will be recorded into hasProved
        Map<FOLExpressionNode, Boolean> hasProved = new HashMap<>();
        return refute(KnowledgeBaseUtil.negateSinglePredicate(expressionNode), onTrack, hasProved);
    }

    private boolean refute(final FOLExpressionNode predicateNode, final Set<FOLExpressionNode> onTrack, final Map<FOLExpressionNode, Boolean> hasProved) {
        // RecursionLimiter.emerge();
        assert KnowledgeBaseUtil.isSinglePredicate(predicateNode);

        if (onTrack.contains(predicateNode)) return false;
        onTrack.add(predicateNode);

        final boolean isAllArgsConstant = KnowledgeBaseUtil.isAllArgsConstant(predicateNode);
        if (isAllArgsConstant && hasProved.containsKey(predicateNode)) {
            return hasProved.get(predicateNode);
        }

        // try to negate each single predicate
        // and search if there is some sentence in the KB that contains that negate predicate
        final FOLExpressionNode negatedPredicate = KnowledgeBaseUtil.negateSinglePredicate(predicateNode);
        final List<Integer> resolutionCandidateIds = this.storage.fetchSentenceIdBySinglePredicate(negatedPredicate);

        for (final int candidateSentenceId : resolutionCandidateIds) {
            final FOLExpressionNode candidateSentence = this.storage.fetchSentenceById(candidateSentenceId);
            final List<FOLExpressionNode> candidatePredicates = KnowledgeBaseUtil.splitSentenceToSinglePredicate(candidateSentence);
            int candidatePredicateIdx = 0;

            for (final FOLExpressionNode candidatePredicate : candidatePredicates) {
                // !!!!!!! Important here
                // the reason we want to UNIFY(candidatePredicate, negatedPredicate) rather than UNIFY(negatedPredicate, candidatePredicate)
                // is because we know negatedPredicate will always been deleted during resolution
                // but there will be other predicates along with candidatePredicate in the following resolution
                // e.g negatedPredicate = ~Parent(y, Billy), candidateSentence = ~Father(x, y) | Parent(x, y)
                //     UNIFY(negatedPredicate, candidatePredicate) = {y/x, Billy/y}
                //     UNIFY(candidatePredicate, negatedPredicate) = {x/y, y/Billy} correct one!
                final Substitution substitution = KnowledgeBaseUnifier.unifyTwoPredicates(
                        candidatePredicate,
                        negatedPredicate
                );

                if (substitution.status == Substitution.Status.SUCCESS) {
                    // once we've successfully generated a new sentence through resolution
                    final FOLExpressionNode sentenceAfterResolution = resolution(
                            predicateNode, 0,
                            candidateSentence, candidatePredicateIdx,
                            substitution
                    );

                    if (sentenceAfterResolution == null) {
                        // we've reached a contradiction
                        // meaning we've successfully refuted the original predicate
                        onTrack.remove(predicateNode);
                        if (isAllArgsConstant) hasProved.put(predicateNode, true);
                        return true;
                    } else {
                        // or we get more predicates to refute in order to refute the original predicate
                        final List<FOLExpressionNode> subPredicatesForRefute = KnowledgeBaseUtil.splitSentenceToSinglePredicate(sentenceAfterResolution);

                        if (subPredicatesForRefute.stream()
                                                  .filter(onTrack::contains)
                                                  .findFirst()
                                                  .orElse(null) != null) {
                            break;
                        }

                        boolean isSuccess = true;
                        for (final FOLExpressionNode onePredicate : subPredicatesForRefute) {
                            // if we failed to refute any one of them
                            // then the whole refutation process for this branch is failed
                            if (!refute(onePredicate, onTrack, hasProved)) {
                                isSuccess = false;
                                break;
                            }
                        }

                        if (isSuccess) {
                            onTrack.remove(predicateNode);
                            if (isAllArgsConstant) hasProved.put(predicateNode, true);
                            return true;
                        }
                    }
                }

                candidatePredicateIdx++;
            }
        }

        onTrack.remove(predicateNode);
        if (isAllArgsConstant) hasProved.put(predicateNode, false);
        return false;
    }

    public FOLExpressionNode resolution(
            final FOLExpressionNode x, final int xPredicateIdx,
            final FOLExpressionNode y, final int yPredicateIdx,
            final Substitution substitution
    ) {
        final List<FOLExpressionNode> singlePredicateX = KnowledgeBaseUtil.splitSentenceToSinglePredicate(x);
        singlePredicateX.remove(xPredicateIdx);

        final List<FOLExpressionNode> singlePredicateY = KnowledgeBaseUtil.splitSentenceToSinglePredicate(y);
        singlePredicateY.remove(yPredicateIdx);

        return Stream.concat(singlePredicateX.stream(), singlePredicateY.stream())
                     .map(substitution::apply)
                     .distinct() // maker sure there is no two same predicates in the result sentence
                     .reduce(null, KnowledgeBaseUtil::concatTwoNodeWithOr);
    }

}
