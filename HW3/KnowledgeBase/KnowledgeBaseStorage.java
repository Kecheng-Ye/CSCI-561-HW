package KnowledgeBase;

import FOLExpression.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

// Basically a trie
// Every ArgumentNode is a fast look-up table for possible values of an argument in a predicate
// Ex. A(Cons1, Cons2, Cons3) --> ArgumentNode[Cons1][Cons2][Cons3]
class ArgumentQueryMap {
    static class ArgumentNode {
        final Map<Integer, ArgumentNode> children;
        boolean isEnd;
        List<Integer> sentenceIds;

        public ArgumentNode() {
            this.children = new HashMap<>();
            this.isEnd = false;
            this.sentenceIds = null;
        }
    }

    final static int VARIABLE_ID = 0;

    final ArgumentNode head;

    ArgumentQueryMap() {
        this.head = new ArgumentNode();
    }

    public void addSentence(final int sentenceId, final List<TermNode> arguments, final Map<ConstantNode, Integer> constantIdMap) {
        ArgumentNode temp = head;

        for (final TermNode argument : arguments) {
            final int argumentId = argument.body.type == FOLExpressionNodeType.VARIABLE ? VARIABLE_ID :
                                   constantIdMap.get((ConstantNode) (argument.body));

            if (!temp.children.containsKey(argumentId)) {
                temp.children.put(argumentId, new ArgumentNode());
            }

            temp = temp.children.get(argumentId);
        }

        temp.isEnd = true;
        if (temp.sentenceIds == null) {
            temp.sentenceIds = new ArrayList<>();
        }
        temp.sentenceIds.add(sentenceId);
    }

    public Set<Integer> matchArgument(final List<TermNode> arguments, final Map<ConstantNode, Integer> constantIdMap) {
        final Set<Integer> result = new HashSet<>();
        matchArgumentHelper(result, 0, head, arguments, constantIdMap);
        return result;
    }

    // DFS
    void matchArgumentHelper(
            final Set<Integer> result,
            final int argNum,
            final ArgumentNode curr,
            final List<TermNode> arguments,
            final Map<ConstantNode, Integer> constantIdMap)
    {
        if (curr == null) {
            return;
        }

        if (argNum == arguments.size()) {
            assert curr.isEnd;
            result.addAll(curr.sentenceIds);
            return;
        }

        final TermNode argument = arguments.get(argNum);
        if (argument.body.type == FOLExpressionNodeType.VARIABLE) {
            for (ArgumentNode child : curr.children.values()) {
                matchArgumentHelper(result, argNum + 1, child, arguments, constantIdMap);
            }
            return;
        }

        final int argumentId = constantIdMap.getOrDefault((ConstantNode) (argument.body), -1);

        if (argumentId != -1)
            matchArgumentHelper(result, argNum + 1, curr.children.getOrDefault(argumentId, null), arguments, constantIdMap);
        matchArgumentHelper(result, argNum + 1, curr.children.getOrDefault(VARIABLE_ID, null), arguments, constantIdMap);
    }
}

public class KnowledgeBaseStorage {
    // [FOL_Sentence_1, FOL_Sentence_2, ....]
    private final List<FOLExpressionNode> sentencesArr;
    // {PredicateName ---> all related sentence}
    private final Map<String, ArgumentQueryMap> predicateArgsQueryMap;
    // {ConstantNode ---> ConstantNodeId}
    private final Map<ConstantNode, Integer> constantIdMap;
    private int constantIdCount;

    public KnowledgeBaseStorage() {
        this.sentencesArr = new ArrayList<>();
        this.constantIdMap = new HashMap<>();
        this.predicateArgsQueryMap = new HashMap<>();
        this.constantIdCount = 1;
    }

    public KnowledgeBaseStorage(List<FOLExpressionNode> sentences) {
        this();
        sentences.forEach(
                sentence -> KnowledgeBaseUtil.splitCNF(FOLExpressionUtil.convertToCNF(sentence))
                                             .forEach(this::addCNFSentence)
        );
    }

    // it is assumed that the input sentence should be in CNF form
    public void addCNFSentence(final FOLExpressionNode expressionNode) {
        int sentenceId = sentencesArr.size();

        if (isSentenceDuplicate(expressionNode)) {
            return;
        }

        addSentenceIntoIndexing(expressionNode, sentenceId);
        sentencesArr.add(expressionNode);
    }

    private void addSentenceIntoIndexing(final FOLExpressionNode expressionNode, final int sentenceId) {
        // for each sentence that is int the form or (A | ~B | C | ...) form
        // split them into list of single predicates
        // e.g (A | ~B | C) => [A, ~B, C]
        final List<FOLExpressionNode> singlePredicates = KnowledgeBaseUtil.splitSentenceToSinglePredicate(expressionNode);

        // build the index on each predicate
        // so that user can query the sentence (A | ~B | C)
        // from either A or ~B or C predicate
        for (final FOLExpressionNode onePredicate : singlePredicates) {
            updateConstantMap(onePredicate);
            final String predicateName = KnowledgeBaseUtil.getSinglePredicateName(onePredicate);
            final List<TermNode> arguments = KnowledgeBaseUtil.getArgumentsFromSinglePredicate(onePredicate);
            ArgumentQueryMap queryMap;
            if (!predicateArgsQueryMap.containsKey(predicateName)) {
                queryMap = new ArgumentQueryMap();
                predicateArgsQueryMap.put(predicateName, queryMap);
            } else {
                queryMap = predicateArgsQueryMap.get(predicateName);
            }

            queryMap.addSentence(
                    sentenceId,
                    arguments,
                    constantIdMap
            );
        }
    }

    public List<Integer> fetchSentenceIdBySinglePredicate(final FOLExpressionNode expressionNode) {
        final List<Integer> result = new ArrayList<>();
        // the input FOL should only a single predicate or a single negated predicate
        final List<TermNode> arguments = KnowledgeBaseUtil.getArgumentsFromSinglePredicate(expressionNode);
        final String predicateName = KnowledgeBaseUtil.getSinglePredicateName(expressionNode);

        if (!predicateArgsQueryMap.containsKey(predicateName)) {
            return result;
        }

        // get the corresponding ArgumentQueryMap based on the predicate name
        final Set<Integer> targetSentenceIds = predicateArgsQueryMap.get(predicateName).matchArgument(arguments, constantIdMap);
        result.addAll(targetSentenceIds);

        return result;
    }

    public List<Integer> fetchSentenceIdByComplexSentence(final FOLExpressionNode expressionNode) {
        final Set<Integer> result = IntStream.range(0, sentencesArr.size()).boxed().collect(Collectors.toSet());
        final List<FOLExpressionNode> singlePredicates = KnowledgeBaseUtil.splitSentenceToSinglePredicate(expressionNode);
        singlePredicates.forEach(predicate -> result.retainAll(fetchSentenceIdBySinglePredicate(predicate)));

        return new ArrayList<>(result);
    }

    private boolean isSentenceDuplicate(final FOLExpressionNode expressionNode) {
        final List<Integer> duplicateCandidates = fetchSentenceIdByComplexSentence(expressionNode);

        for (final int duplicateCandidateId : duplicateCandidates) {
            if (sentencesArr.get(duplicateCandidateId).equals(expressionNode)) return true;
        }

        return false;
    }

    public FOLExpressionNode fetchSentenceById(final int sentenceId) {
        assert sentenceId >=0 && sentenceId < sentencesArr.size();
        return this.sentencesArr.get(sentenceId);
    }

    private void updateConstantMap(final FOLExpressionNode expressionNode) {
        final List<TermNode> arguments = KnowledgeBaseUtil.getArgumentsFromSinglePredicate(expressionNode);

        for (final TermNode argument : arguments) {
            if (argument.body.type == FOLExpressionNodeType.CONSTANT) {
                ConstantNode node = (ConstantNode) argument.body;
                if (!constantIdMap.containsKey(node)) {
                    constantIdMap.put(node, constantIdCount++);
                }
            }
        }
    }

    public void addAllSentenceIdIntoContainer(final Collection<Integer> collection) {
        collection.addAll(IntStream.range(0, sentencesArr.size()).boxed().collect(Collectors.toList()));
    }

}
