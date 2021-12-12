package day12

import readInput


fun main() {
    val edges = readInput("day12/Day12")
        .map { it.split('-').let { (from, to) -> CaveEdge(Pair(CaveVertex(from), CaveVertex(to))) } }
    val graph = Graph.from(edges)
    val source = CaveVertex.start()
    val destination = CaveVertex.end()

    // part 1
    val allPaths = graph.findAllPaths(source, destination)
    println(allPaths.size)

    // part 2
    val uniquePaths = mutableSetOf<List<CaveVertex>>()
    for (smallCave in graph.vertices().filter { it.type == VertexType.SMALL }) {
        val paths = graph.findAllPaths(source, destination) { cave, count -> cave == smallCave && count < 2 }
        uniquePaths.addAll(paths)
    }
    println(uniquePaths.size)

}

data class CaveVertex(val value: String) {
    val type: VertexType
        get() = when (value) {
            "start" -> VertexType.START
            "end" -> VertexType.END
            else -> if (value == value.uppercase()) VertexType.BIG else VertexType.SMALL
        }

    override fun toString() = value

    companion object {
        fun start() = CaveVertex("start")
        fun end() = CaveVertex("end")
    }
}

enum class VertexType {
    BIG, SMALL, START, END
}

data class CaveEdge(val vertices: Pair<CaveVertex, CaveVertex>)

class Graph {
    private val adjacencyList = mutableMapOf<CaveVertex, MutableSet<CaveVertex>>()

    fun findAllPaths(
        source: CaveVertex,
        destination: CaveVertex,
        predicate: (CaveVertex, Int) -> Boolean = { _: CaveVertex, _: Int -> false }
    ): Set<List<CaveVertex>> {
        val allPaths = mutableSetOf<List<CaveVertex>>()
        val currentPath = listOf(source)
        findAllPathsRec(currentPath, allPaths, destination, predicate)
        return allPaths
    }

    private fun findAllPathsRec(
        currentPath: List<CaveVertex>,
        allPaths: MutableSet<List<CaveVertex>>,
        destination: CaveVertex,
        predicate: (CaveVertex, Int) -> Boolean
    ) {
        val last = currentPath.last()
        if (last == destination) {
            allPaths.add(currentPath)
            return
        }

        val potentialNeighbours = adjacencyList[last]
            ?.filter { cave -> cave.type != VertexType.START }
            ?.filter { cave ->
                cave.type == VertexType.BIG
                        || !currentPath.contains(cave)
                        || predicate(cave, currentPath.count { it == cave })
            } ?: emptySet()
        
        for (neighbour in potentialNeighbours) {
            findAllPathsRec(currentPath + neighbour, allPaths, destination, predicate)
        }
    }

    fun vertices() = adjacencyList.keys

    private fun addAll(edges: List<CaveEdge>) = edges.forEach { this.add(it) }

    private fun add(edge: CaveEdge) {
        val (v1, v2) = edge.vertices
        // graph is bidirectional
        add(v1, v2)
        add(v2, v1)
    }

    private fun add(v1: CaveVertex, v2: CaveVertex) {
        val neighbours = (adjacencyList[v1] ?: mutableSetOf()).also { it.add(v2) }
        adjacencyList[v1] = neighbours
    }

    override fun toString() = "Graph(adjacencyList=(${adjacencyList.map { entry -> "\n\t$entry" }}\n))"

    companion object {
        fun from(edges: List<CaveEdge>) = Graph().also { it.addAll(edges) }
    }

}