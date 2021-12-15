package day15

import readInput

fun main() {
    val matrix = readInput("day15/Day15")
        .map { line -> line.toList().map { char -> char.toString().toInt() } }
    val n = matrix[0].size
    val m = matrix.size

    val graph1 = Graph.from(matrix)
    val cost1 = dijkstraShortestPathCost(graph1, Vertex(0, 0), Vertex(n - 1, m - 1))
    println(cost1)

    val matrix2 = explode(matrix)
    val n2 = matrix[0].size
    val m2 = matrix.size
    val graph2 = Graph.from(matrix2)
    val cost2 = dijkstraShortestPathCost(graph2, Vertex(0, 0), Vertex(n2 - 1, m2 - 1))
    println(cost2)
}

fun dijkstraShortestPathCost(graph: Graph, source: Vertex, destination: Vertex): Int {
    val adjacencyList = graph.adjacencyList
    val queue = mutableListOf(source)
    val minDistances = graph.vertices.fold(mutableMapOf<Vertex, Int>()) { acc, vertex ->
        acc[vertex] = Int.MAX_VALUE
        acc
    }
    minDistances[source] = 0
    val predecessor = mutableMapOf<Vertex, Vertex>()

    while (queue.isNotEmpty()) {
        val current = queue.removeLast()
        for (neighbour in adjacencyList[current]?.let { it.keys } ?: emptySet()) {
            val newDistance = minDistances[current]!! + adjacencyList[current]!![neighbour]!!

            if (newDistance < minDistances[neighbour]!!) {
                minDistances[neighbour] = minOf(minDistances[neighbour]!!, newDistance)
                queue.add(neighbour)
                predecessor[neighbour] = current
            }
        }
    }
    return minDistances[destination]!!
}

fun explode(matrix: List<List<Int>>) = matrix

data class Vertex(val i: Int, val j: Int)
data class Edge(val from: Vertex, val to: Vertex, val cost: Int)

data class Graph(val vertices: Set<Vertex>, val edges: List<Edge>) {
    val adjacencyList: Map<Vertex, Map<Vertex, Int>>
        get() {
            val result = mutableMapOf<Vertex, MutableMap<Vertex, Int>>()
            for ((from, to, cost) in edges) {
                val neighboursFrom = result[from] ?: mutableMapOf()
                neighboursFrom[to] = cost
                result[from] = neighboursFrom
            }
            return result.toMap()
        }

    companion object {
        fun from(matrix: List<List<Int>>): Graph {
            val vertices = mutableSetOf<Vertex>()
            val edges = mutableListOf<Edge>()
            val n = matrix[0].size
            val m = matrix.size
            for (j in 0 until m) {
                for (i in 0 until n) {
                    val vertex = Vertex(i, j)
                    vertices.add(vertex)
                    val edge1 = if (i < n - 1) Edge(vertex, Vertex(i + 1, j), matrix[j][i + 1]) else null
                    val edge2 = if (j < m - 1) Edge(vertex, Vertex(i, j + 1), matrix[j + 1][i]) else null
                    edge1?.also { edge -> edges.add(edge) }
                    edge2?.also { edge -> edges.add(edge) }
                }
            }
            return Graph(vertices.toSet(), edges.toList())
        }
    }

    override fun toString() =
        """Graph(vertices=(${vertices.map { vertex -> "\n\t$vertex" }})), edges=(${edges.map { edge -> "\n\t$edge" }}))
        """
}