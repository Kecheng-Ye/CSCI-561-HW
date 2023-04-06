package Test;

import FOLExpression.FOLExpressionNode;
import FOLParser.FOLParser;
import KnowledgeBase.KnowledgeBase;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class KnowledgeBaseTest {
    final FOLParser parser = new FOLParser();

    @Test
    void tell() {
    }

    @Test
    void ask1() {
        KnowledgeBase kb = new KnowledgeBase();
        final List<FOLExpressionNode> sentences = new ArrayList<>() {{
            add(parser.parse("~Cat(x) | Likes(x,Fish)"));
            add(parser.parse("~Cat(y) | ~Likes(y, z) | Eats(y, z)"));
            add(parser.parse("Cat(Jo)"));
        }};

        sentences.forEach(kb::tell);
        assertTrue(kb.ask(parser.parse("Eats(Jo, Fish)")));
    }

    @Test
    void ask2() {
        KnowledgeBase kb = new KnowledgeBase();
        final List<FOLExpressionNode> sentences = new ArrayList<>() {{
            add(parser.parse("Dog(d)"));
            add(parser.parse("Owns(Jack, d)"));
            add(parser.parse("Dog(y) & Owns(x, y) => AnimalLover(x)"));
            add(parser.parse("~AnimalLover(x) | ~Animal(y) | ~Kills(x, y)"));
            add(parser.parse("Kills(Jack, Tuna) | Kills(Curiosity, Tuna)"));
            add(parser.parse("Cat(Tuna)"));
            add(parser.parse("Cat(x) => Animal(x)"));
        }};

        sentences.forEach(kb::tell);
        assertTrue(kb.ask(parser.parse("Kills(Curiosity, Tuna)")));
    }

    @Test
    void ask3() {
        KnowledgeBase kb = new KnowledgeBase();
        final List<FOLExpressionNode> sentences = new ArrayList<>() {{
            add(parser.parse("~Parent(x, y) | ~Parent(y, z) | Ancestor(x, z)"));
            add(parser.parse("~Parent(x, y) | Ancestor(x, y)"));
            add(parser.parse("~Mother(x, y) | Parent(x, y)"));
            add(parser.parse("~Father(x, y) | Parent(x, y)"));
            add(parser.parse("Mother(Liz, Charley)"));
            add(parser.parse("Father(Charley, Billy)"));
        }};

        sentences.forEach(kb::tell);
        assertTrue(kb.ask(parser.parse("Ancestor(Liz, Billy)")));
    }

    @Test
    void ask4() {
        KnowledgeBase kb = new KnowledgeBase();
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

        sentences.forEach(kb::tell);
        assertTrue(kb.ask(parser.parse("Leave(Helena)")));
    }

    @Test
    void ask5() {
        KnowledgeBase kb = new KnowledgeBase();
        final List<FOLExpressionNode> sentences = new ArrayList<>() {{
            add(parser.parse("~Order(x,y) | Seated(x)"));
            add(parser.parse("~Order(x,y) | Stocked(y)"));
            add(parser.parse("~Ate(x) | GetCheck(x)"));
            add(parser.parse("~GetCheck(x) | ~Paid(x) | Leave(x)"));
            add(parser.parse("~Seated(x) | Open(Restaurant)"));
            add(parser.parse("~Seated(x) | Open(Kitchen)"));
            add(parser.parse("Stocked(Hamburger)"));
            add(parser.parse("Open(Restaurant)"));
            add(parser.parse("Open(Kitchen)"));
        }};

        sentences.forEach(kb::tell);
        assertFalse(kb.ask(parser.parse("Order(Jenny,Pizza)")));
    }


    @Test
    void ask7() {
        KnowledgeBase kb = new KnowledgeBase();
        final List<FOLExpressionNode> sentences = new ArrayList<>() {{
            add(parser.parse("Likes(x,y) & Likes(y,x) | Meet(x,y,z) => Hangout(x,y)"));
            add(parser.parse("Leave(x,z) & Leave(y,z) => Meet(x,y,z)"));
            add(parser.parse("GetCheck(x,z) & Paid(x,z) => Leave(x,z)"));
            add(parser.parse("GetCheck(x,z) & HaveMoney(x) => Paid(x,z)"));
            add(parser.parse("Ate(x,y) => GetCheck(x,z)"));
            add(parser.parse("Order(x,y) & Good(y) => Ate(x,y)"));
            add(parser.parse("Seated(x,z) & Stocked(y,z) => Order(x,y)"));
            add(parser.parse("OpenRestaurant(z) & Open(Kitchen,z) & HasTable(z) => Seated(x,z)"));
            add(parser.parse("TableOpen(x,z) | TableOpen(y,z) => HasTable(z)"));
            add(parser.parse("HasIngredients(y,z) & Open(Kitchen,z) => Stocked(y,z)"));
            add(parser.parse("~Bad(x) => Good(x)"));
            add(parser.parse("Has(Dough,z) & Has(Cheese,z) => HasIngredients(CheesePizza,z)"));
            add(parser.parse("Has(Pasta,z) & Has(Pesto,z) => HasIngredients(PestoPasta,z)"));
            add(parser.parse("Has(Falafel,z) & Has(Hummus,z) => HasIngredients(FalafelPlate,z)"));
            add(parser.parse("Has(Rice,z) & Has(Lamb,z) => HasIngredients(LambPlate,z)"));
            add(parser.parse("Has(LadyFingers,z) & Has(Mascarpone,z) => HasIngredients(Tiramisu,z)"));
            add(parser.parse("Old(Cheese) | Burnt(CheesePizza) => Bad(CheesePizza)"));
            add(parser.parse("Moldy(Pesto) => Bad(PestoPasta)"));
            add(parser.parse("Bad(Lamb) | Soggy(Rice) => Bad(LambPlate)"));
            add(parser.parse("Has(Dough,Bestia)"));
            add(parser.parse("Has(Cheese,Bestia)"));
            add(parser.parse("Has(Cheese,Dune)"));
            add(parser.parse("Has(Pasta,Bestia)"));
            add(parser.parse("Has(Pesto,Bestia)"));
            add(parser.parse("Has(Falafel,Dune)"));
            add(parser.parse("Has(Hummus,Dune)"));
            add(parser.parse("Has(Rice,Dune)"));
            add(parser.parse("Has(Lamb,Dune)"));
            add(parser.parse("Has(LadyFingers,Bestia)"));
            add(parser.parse("Has(Mascarpone,Bestia)"));
            add(parser.parse("Burnt(CheesePizza)"));
            add(parser.parse("Soggy(Rice)"));
            add(parser.parse("~Bad(Tiramisu)"));
            add(parser.parse("Bad(Lamb)"));
            add(parser.parse("OpenRestaurant(Bestia)"));
            add(parser.parse("Open(Kitchen,Bestia)"));
            add(parser.parse("OpenRestaurant(Dune)"));
            add(parser.parse("Open(Kitchen,Dune)"));
            add(parser.parse("HaveMoney(Leia)"));
            add(parser.parse("HaveMoney(Teddy)"));
            add(parser.parse("Likes(Leia,Teddy)"));
            add(parser.parse("Likes(Leia,Mary)"));
            add(parser.parse("Likes(Teddy,Harry)"));
            add(parser.parse("Likes(Harry,Teddy)"));
            add(parser.parse("TableOpen(Patio,Bestia)"));
        }};

        sentences.forEach(kb::tell);
        assertTrue(kb.ask(parser.parse("Hangout(Leia,Teddy)")));
    }

    @Test
    void ask8() {
        KnowledgeBase kb = new KnowledgeBase();
        final List<FOLExpressionNode> sentences = new ArrayList<>() {{
            add(parser.parse("A(x) => B(x)"));
            add(parser.parse("B(x) => A(x)"));
        }};

        sentences.forEach(kb::tell);
        assertFalse(kb.ask(parser.parse("A(Teddy)")));
    }
}