package Test;

import FOLExpression.FOLExpressionNode;
import FOLExpression.FOLExpressionUtil;
import FOLParser.FOLParser;
import KnowledgeBase.KnowledgeBaseStorage;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class KnowledgeBaseStorageTest {

    final FOLParser parser = new FOLParser();

    final List<FOLExpressionNode> sentences = new ArrayList<>() {{
       add(parser.parse("Seated(x) & Stocked(y) => Order(x,y)"));
       add(parser.parse("Order(x,y) => Ate(x)"));
       add(parser.parse("GetCheck(x) & HaveMoney(x) => Paid(x)"));
       add(parser.parse("Ate(x) => GetCheck(x)"));
       add(parser.parse("GetCheck(x) & Paid(x) => Leave(x)"));
       add(parser.parse("Open(Restaurant) & Open(Kitchen) => Seated(x)"));
       add(parser.parse("Stocked(Portabello) | Stocked(Tofu) => Stocked(VeganHamburger)"));
       add(parser.parse("Stocked(Portabello)"));
       add(parser.parse("Open(Restaurant)"));
       add(parser.parse("Open(Kitchen)"));
       add(parser.parse("HaveMoney(Helena)"));
    }};

    @Test
    void addSentence() {
        final KnowledgeBaseStorage storage = new KnowledgeBaseStorage(sentences);
    }

    @Test
    void fetchSentenceBySinglePredicate() {
        final KnowledgeBaseStorage storage = new KnowledgeBaseStorage(sentences);
        storage.fetchSentenceIdBySinglePredicate(parser.parse("~HaveMoney(x)"))
                .stream()
                .map(storage::fetchSentenceById)
                .forEach(FOLExpressionUtil::printExpression);
    }

    @Test
    void fetchSentenceByComplexSentence() {
        final KnowledgeBaseStorage storage = new KnowledgeBaseStorage(sentences);
        storage.fetchSentenceIdByComplexSentence(parser.parse("~Seated(x) | ~Stocked(y)"))
                .stream()
                .map(storage::fetchSentenceById)
                .forEach(FOLExpressionUtil::printExpression);
    }
}