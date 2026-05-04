import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class GraphTest {

    @Test
    void shouldDetectCycleInCyclicGraph() {
        // Arrange (Підготовка)
        Graph<Integer> graphWithCycle = new Graph<>();
        graphWithCycle.addEdge(0, 1);
        graphWithCycle.addEdge(1, 2);
        graphWithCycle.addEdge(2, 3);
        graphWithCycle.addEdge(3, 0); // Замикання циклу
        graphWithCycle.addEdge(2, 4);

        // Act (Дія)
        boolean hasCycle = graphWithCycle.hasCycle();

        // Assert (Перевірка)
        assertTrue(hasCycle, "Граф із замкнутим контуром повинен містити цикл");
    }

    @Test
    void shouldNotDetectCycleInAcyclicGraph() {
        // Arrange
        Graph<Integer> treeGraph = new Graph<>();
        treeGraph.addEdge(0, 1);
        treeGraph.addEdge(0, 2);
        treeGraph.addEdge(1, 3);

        // Act
        boolean hasCycle = treeGraph.hasCycle();

        // Assert
        assertFalse(hasCycle, "Дерево (ациклічний граф) не повинно містити циклів");
    }

    @Test
    void shouldFindShortestPathAndCalculateDistancesCorrectly() {
        // Arrange
        Graph<Integer> city = new Graph<>();
        city.addEdge(0, 1);
        city.addEdge(0, 3);
        city.addEdge(1, 2);
        city.addEdge(1, 4);
        city.addEdge(2, 5);
        city.addEdge(3, 4);
        city.addEdge(4, 6);
        city.addEdge(5, 7);
        city.addEdge(6, 7);

        // Act: Найкоротший шлях
        List<Integer> shortestPath = city.shortestPathBfs(0, 7);

        // Assert: Найкоротший шлях
        assertFalse(shortestPath.isEmpty(), "Шлях до цільової вершини повинен існувати");
        assertEquals(0, shortestPath.get(0), "Шлях має починатися з вершини 0");
        assertEquals(7, shortestPath.get(shortestPath.size() - 1), "Шлях має закінчуватися вершиною 7");
        assertEquals(5, shortestPath.size(), "Мінімальний шлях має містити рівно 5 вершин (4 ребра)");

        // Act: Відстані (рівні)
        Map<Integer, Integer> distances = city.bfsDistances(0);

        // Assert: Відстані
        assertNotNull(distances, "Мапа відстаней не повинна бути null");
        assertEquals(0, distances.get(0), "Відстань до самої себе має бути 0");
        assertEquals(1, distances.get(3), "Відстань 0-3 має бути 1");
        assertEquals(4, distances.get(7), "Найкоротша відстань до 7 має бути 4 ребра");
    }

    @Test
    void shouldFindValidDfsPath() {
        // Arrange
        Graph<Integer> city = new Graph<>();
        city.addEdge(0, 1);
        city.addEdge(1, 2);
        city.addEdge(2, 7);

        // Act
        List<Integer> dfsPath = city.dfsPath(0, 7);

        // Assert
        assertFalse(dfsPath.isEmpty(), "DFS шлях повинен існувати");
        assertEquals(0, dfsPath.get(0), "Шлях має починатися зі стартової вершини");
        assertEquals(7, dfsPath.get(dfsPath.size() - 1), "Шлях має закінчуватися цільовою вершиною");
    }

    @Test
    void shouldCorrectlyIdentifyBipartiteGraphs() {
        // Arrange: Двочастковий граф (квадрат)
        Graph<Integer> bipartiteGraph = new Graph<>();
        bipartiteGraph.addEdge(0, 1);
        bipartiteGraph.addEdge(1, 2);
        bipartiteGraph.addEdge(2, 3);
        bipartiteGraph.addEdge(3, 0);

        // Arrange: Не двочастковий граф (трикутник)
        Graph<Integer> nonBipartiteGraph = new Graph<>();
        nonBipartiteGraph.addEdge(0, 1);
        nonBipartiteGraph.addEdge(1, 2);
        nonBipartiteGraph.addEdge(2, 0); // Непарний цикл

        // Act & Assert
        assertTrue(bipartiteGraph.isBipartite(), "Граф з парними циклами є двочастковим");
        assertFalse(nonBipartiteGraph.isBipartite(), "Граф з непарним циклом не може бути двочастковим");
    }

    @Test
    void shouldHandleEdgeCasesSafely() {
        // Arrange
        Graph<Integer> graph = new Graph<>();
        graph.addEdge(0, 1);

        // Act & Assert: Шлях до неіснуючої вершини
        List<Integer> pathToNowhere = graph.shortestPathBfs(0, 99);
        assertTrue(pathToNowhere.isEmpty(), "Шлях до неіснуючої вершини має повертати пустий список");

        // Act & Assert: Шлях від неіснуючої вершини
        List<Integer> pathFromNowhere = graph.shortestPathBfs(99, 1);
        assertTrue(pathFromNowhere.isEmpty(), "Шлях від неіснуючої вершини має повертати пустий список");

        // Act & Assert: Шлях до самої себе
        List<Integer> pathSelf = graph.shortestPathBfs(0, 0);
        assertEquals(1, pathSelf.size(), "Шлях до себе складається з 1 вершини");
        assertEquals(0, pathSelf.get(0), "Вершина має дорівнювати самій собі");

        // Act & Assert: Відстані для неіснуючої вершини
        Map<Integer, Integer> unknownDistances = graph.bfsDistances(99);
        assertTrue(unknownDistances.isEmpty(), "Відстані для неіснуючої вершини мають бути порожньою мапою");
    }
}
