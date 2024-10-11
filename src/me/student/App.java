package me.student;

import me.student.WeightedGraph.Graph;

public class App {
    public static void main(String[] args) throws Exception {
        Graph g = new Graph(10);
        Coordinates3D startNode = SalesmanProblemAlgorithms.getStartNode(g);
        System.out.println("graph edges reduced by: " + g.reduceEdges(20) + "%");
        // g.printGraph();

        // [ToDo]
        // Something is wrong with DFS method. Sometime it
        // retruns path that is longer than BFS method
        Infos dfs = SalesmanProblemAlgorithms.tryDFS(g, startNode);
        System.out.println("DFS -> " + dfs.toString());

        // just works :)
        Infos bfs = SalesmanProblemAlgorithms.tryBFS(g, startNode);
        System.out.println("BFS -> " + bfs.toString());

        // Because it is always taking the shortest path it
        // not always retruns all the nodes. The last node 
        // it's visiting might not be connected to some
        // remaning nodes.
        Infos nn = SalesmanProblemAlgorithms.tryNN(g, startNode);
        System.out.println(" NN -> " + nn.toString());
    }
}
