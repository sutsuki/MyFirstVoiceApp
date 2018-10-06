package jp.ac.nitech.club.c0de.myfirstvoiceapp.search

import java.util.*

/**
 * 分枝限定法を用いた探索
 * @author t_kamiya
 * Created on 2018/10/04
 */
object Search {
    data class Result(
            val found: Boolean,
            val node: Node?,
            val step: Int
    )

    fun search(from: Place, to: Place): Result {
        val space = StateSpace(from, to)

        val openList = PriorityQueue<Node>(space.nodes.size, compareBy { it.gValue })
        val closedList = mutableSetOf<Node>()

        space.start.gValue = 0
        openList.offer(space.start)

        var step = 0
        while (openList.isNotEmpty()) {
            step++

            val node = openList.poll()!!
            closedList.add(node)

            if (node == space.goal) {
                return Result(true, node, step)
            }

            for (child in node.children) {
                val g = node.gValue!! + node.getCost(child)!!
                fun assign() {
                    child.gValue = g
                    child.parent = node
                }

                if (!openList.contains(child) && !closedList.contains(child)) {
                    // 初めて見つけたノード
                    //assign()
                } else if (openList.contains(child)) {
                    // 発見済みのノード
                    if (g < child.gValue!!) {
                        //assign()
                        openList.remove(child)
                    } else {
                        continue
                    }
                } else if (closedList.contains(child)) {
                    // 探索済みのノード
                    continue
                }

                assign()
                openList.offer(child)
            }
        }
        return Result(false, null, step)
    }
}