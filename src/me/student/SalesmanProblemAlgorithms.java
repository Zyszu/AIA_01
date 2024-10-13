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
        Coordinates3DLinkedList shortestPath = null;
        Queue<Coordinates3DLinkedList> queue = new LinkedList<>();

        Coordinates3DLinkedList path = new Coordinates3DLinkedList();
        path.add(startNode);
        queue.add(path);

        while (!queue.isEmpty()) {
            path = queue.poll();
            Coordinates3D at = path.getLast();

            if(path.size() == graph.vertices) {
                path.add(startNode);
                if(shortestPath == null) {
                    shortestPath = path;
                } else {
                    if (shortestPath.getPathDistance() > path.getPathDistance())
                        shortestPath = path;
                }
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
                if(at.getDistanceTo(next) < shortestDistance) {
                    closesCoordinates3d = next;
                    shortestDistance = at.getDistanceTo(next);
                }

            }
            at = closesCoordinates3d;
        }
        shortestPath.add(startNode);
        return shortestPath;
    }
    
    public static Infos tryAStar(Graph graph, Coordinates3D startNode) {
        long tStart = System.currentTimeMillis();

        Coordinates3DLinkedList shortestPath = aStar(graph, startNode);

        long tEnd = System.currentTimeMillis();
        return new Infos(tEnd - tStart, 0, shortestPath);
    }

    private static Coordinates3D getFarthestC3d(Coordinates3D at, List<Coordinates3D> c3dll) {
        if(c3dll == null)   return null;
        if(c3dll.isEmpty()) return null;

        Coordinates3D farthestC3d = c3dll.removeFirst();

        for(Coordinates3D c3d : c3dll) {
            if(at.getDistanceTo(c3d) > at.getDistanceTo(farthestC3d))
                farthestC3d = c3d;
        }

        return farthestC3d;
    }

    private static Coordinates3DLinkedList aStar(Graph graph, Coordinates3D startNode) {
        List<Coordinates3D> visited = new ArrayList<>();
        List<Coordinates3D> unVisited = new ArrayList<>(graph.verticesList);

        Coordinates3DLinkedList shortestPath = new Coordinates3DLinkedList();
        Coordinates3D at = startNode;

        Double heuristicInfluence = 100.0;

        while (at != null) {
            visited.add(at);
            unVisited.remove(at);
            shortestPath.add(at);

            Coordinates3D closesCoordinates3d = null;
            Double distanceShortest = Double.MAX_VALUE;

            List<Edge> edgesList = graph.adjacencylist.get(at);
            for(Edge e : edgesList) {
                Coordinates3D next = e.destination;
                if(visited.contains(next)) continue;

                Double heuristicNext     = 0.0;
                Double heuristicShortest = 0.0;

                if(closesCoordinates3d != null) {
                    Coordinates3D fc = getFarthestC3d(at, unVisited);
                    if(fc != null) {
                        heuristicNext     =                next.getDistanceTo(fc) * heuristicInfluence;
                        heuristicShortest = closesCoordinates3d.getDistanceTo(fc) * heuristicInfluence;
                    }
                }

                Double d1 = at.getDistanceTo(next) + heuristicNext;
                Double d2 = distanceShortest + heuristicShortest;

                if(d1 < d2) {
                    closesCoordinates3d = next;
                    distanceShortest = at.getDistanceTo(next);
                }
            }
            at = closesCoordinates3d;
        }
        shortestPath.add(startNode);
        return shortestPath;
    }

    public static Infos tryACO(Graph graph, Coordinates3D startNode) {
        ACO();
        return null;
    }

    private static Coordinates3DLinkedList ACO() {
        return null;
    }

}
