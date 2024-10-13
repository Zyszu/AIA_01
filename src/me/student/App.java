package me.student;

import me.student.WeightedGraph.Graph;

public class App {
    public static void main(String[] args) throws Exception {
        Boolean symetrical = false;
        Graph g = new Graph(12);
        Coordinates3D startNode = SalesmanProblemAlgorithms.getStartNode(g);

        if(!symetrical)
            System.out.println("graph edges reduced by: " + g.reduceEdges(20) + "%");
        // g.printGraph();

        // Because it is always taking the shortest path it
        // not always retruns all the nodes. The last node 
        // it's visiting might not be connected to some
        // remaning nodes.
        Infos nn = SalesmanProblemAlgorithms.tryNN(g, startNode);
        System.out.println(" NN -> " + nn.toString());

        Infos aStar = SalesmanProblemAlgorithms.tryAStar(g, startNode);
        System.out.println(" A* -> " + aStar.toString());

        if(
            (g.vertices > 12 && !symetrical) ||
            (g.vertices > 11 && symetrical)
        ) return;

        // [ToDo]
        // Something is wrong with DFS method. Sometime it
        // retruns path that is longer than BFS method
        Infos dfs = SalesmanProblemAlgorithms.tryDFS(g, startNode);
        System.out.println("DFS -> " + dfs.toString());
        // System.out.println(dfs.getPath().toString());

        // just works :)
        Infos bfs = SalesmanProblemAlgorithms.tryBFS(g, startNode);
        System.out.println("BFS -> " + bfs.toString());
        // System.out.println(bfs.getPath().toString());

    }
}
