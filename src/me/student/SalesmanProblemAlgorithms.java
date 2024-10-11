package me.student;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import me.student.Coordinates3D.Coordinates3DLinkedList;
import me.student.WeightedGraph.Edge;
import me.student.WeightedGraph.Graph;

public class SalesmanProblemAlgorithms {

    public static Coordinates3D getStartNode(Graph graph) {
        if(graph.adjacencylist.size() == 0) return null;
        for(Coordinates3D c3d : graph.adjacencylist.keySet()) return c3d;
        return null;
    }

    public static Infos tryDFS(Graph graph, Coordinates3D startNode) {
        long tStart = System.currentTimeMillis();

        List<Coordinates3D> isVisited = new ArrayList<>();
        Coordinates3DLinkedList shortestPath = DFS(startNode, startNode, graph, isVisited);

        long tEnd = System.currentTimeMillis();
        return new Infos(tEnd - tStart, 0, shortestPath);
    }

    private static Coordinates3DLinkedList DFS(
        Coordinates3D at,
        Coordinates3D startNode,
        Graph graph,
        List<Coordinates3D> isVisited
        ) {
        isVisited.add(at);

        Coordinates3DLinkedList shortestPath = new Coordinates3DLinkedList();
        List<Coordinates3DLinkedList> paths = new ArrayList<>();

        LinkedList<Edge> atList = graph.adjacencylist.get(at);
        for(Edge e : atList) {
            Coordinates3D next = e.destination;
            if(isVisited.contains(next)) continue;

            Coordinates3DLinkedList newPath = DFS(next, startNode, graph, isVisited);
            newPath.addFirst(at);
            paths.add(newPath);
        }

        // no more unvisited nodes -> return starting one
        if(paths.isEmpty()) {
            shortestPath.addFirst(startNode);
            shortestPath.addFirst(at);
            isVisited.remove(at);
            return shortestPath;
        }

        shortestPath = paths.removeFirst();
        for(Coordinates3DLinkedList c3dll : paths) {
            if(
                c3dll.getPathDistance() < shortestPath.getPathDistance() &&
                // if not equal then means that algorythm skipped some node
                c3dll.size() == graph.vertices + 2 - isVisited.size()
            )
                shortestPath = c3dll;
        }

        isVisited.remove(at);
        return shortestPath;
    }

    public static Infos tryBFS(Graph graph, Coordinates3D startNode) {
        long tStart = System.currentTimeMillis();

        Coordinates3DLinkedList shortestPath = BFS(graph, startNode);

        long tEnd = System.currentTimeMillis();
        return new Infos(tEnd-tStart, 0, shortestPath);
    }

    public static Coordinates3DLinkedList BFS(Graph graph, Coordinates3D startNode) {
        List<Coordinates3DLinkedList> paths = new ArrayList<>();
        Queue<Coordinates3DLinkedList> queue = new LinkedList<>();

        Coordinates3DLinkedList path = new Coordinates3DLinkedList();
        path.add(startNode);
        queue.add(path);

        while (!queue.isEmpty()) {
            path = queue.poll();
            Coordinates3D at = path.getLast();

            // If path contains all nodes it adds path to list of
            // all possible paths.
            // [ToDO] [optimization]
            // There is no need to keep all paths in memmory.
            // Here we coud run a check if this new path is shorter
            // than the current shortest path. If it is we could
            // make new path a new shortest path. I keep it like
            // that to check how much memmory this approach will
            // consume.
            if(path.size() == graph.vertices) {
                path.add(startNode);
                paths.add(path);
            }

            List<Edge> edges = graph.adjacencylist.get(at);
            for(Edge e : edges) {
                Coordinates3D next = e.destination;
                if(path.contains(next)) continue;

                Coordinates3DLinkedList newPath = new Coordinates3DLinkedList(path);
                newPath.add(next);
                queue.add(newPath);
            }
            
        }

        Coordinates3DLinkedList shortestPath = paths.removeFirst();

        for(Coordinates3DLinkedList c3dll : paths) {
            if(
                c3dll.getPathDistance() < shortestPath.getPathDistance() &&
                c3dll.size() == graph.vertices + 1
            )
            shortestPath = c3dll;
        }


        return shortestPath;
    }

    public static Infos tryNN(Graph graph, Coordinates3D startNode) { 
        long tStart = System.currentTimeMillis();
        Coordinates3DLinkedList shortestPath = NN(graph, startNode);
        long tEnd = System.currentTimeMillis();
        return new Infos(tEnd - tStart, 0, shortestPath);
    }

    private static Coordinates3DLinkedList NN(Graph graph, Coordinates3D startNode) {
        List<Coordinates3D> isVisited = new ArrayList<>();
        Coordinates3DLinkedList shortestPath = new Coordinates3DLinkedList();
        Coordinates3D at = startNode;

        while (at != null) {
            isVisited.add(at);
            shortestPath.add(at);

            Coordinates3D closesCoordinates3d = null;
            Double shortestDistance = Double.MAX_VALUE;

            List<Edge> edgesList = graph.adjacencylist.get(at);
            for(Edge e : edgesList) {
                Coordinates3D next = e.destination;
                if(isVisited.contains(next)) continue;
                if(at.getDistanceTo(next) < shortestDistance)
                    closesCoordinates3d = next;
            }
            at = closesCoordinates3d;
        }
        shortestPath.add(startNode);
        return shortestPath;
    }
    
}
