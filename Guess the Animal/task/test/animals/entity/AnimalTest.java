package animals.entity;

import org.junit.Test;

import static animals.entity.Animal.*;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

// JUnit5 parametrized tests would provide more meaningful coverage, but this doesn't currently work in tandem with
// the existing Hyperskill test config
public class AnimalTest {

    @Test
    public void determineArticleStartsWithVowel() {
        assertEquals("an", determineArticle("elephant"));
    }

    @Test
    public void determineArticleMultisectionName() {
        assertEquals("a", determineArticle("xantic sargo"));
    }

    @Test
    public void determineArticleBStartsWithConsonant() {
        assertEquals("a", determineArticle("bear"));
    }

    @Test
    public void testAnimalObjectCreation() { // tests the end-to-end workflow composed of the above methods
        Animal animal = new Animal("cat");
        assertEquals("a cat", animal.toString());
    }

    @Test
    public void testAnimalObjectCreationWithMultiwordName() { // tests the end-to-end workflow composed of the above methods
        Animal animal = new Animal("the xantic sargo");
        assertEquals("a xantic sargo", animal.toString());
    }

    @Test
    public void testAnimaObjecCreationtWithMultiwordNameNoArticle() { // tests the end-to-end workflow composed of the above methods
        Animal animal = new Animal("xantic sargo");
        assertEquals("a xantic sargo", animal.toString());
    }
}