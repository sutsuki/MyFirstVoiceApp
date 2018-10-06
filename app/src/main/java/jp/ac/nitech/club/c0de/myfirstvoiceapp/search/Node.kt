package jp.ac.nitech.club.c0de.myfirstvoiceapp.search

/**
 *
 * @author t_kamiya
 * Created on 2018/10/04
 */
class Node(
        /** 場所 */
        val place: Place
): Cloneable {
    var gValue: Int? = null

    /** 子ノードのコスト */
    private val childrenCosts: MutableMap<Node, Int> = mutableMapOf()
    /** 子ノード */
    val children = childrenCosts.keys
    /** 親ノード */
    var parent: Node? = null

    fun addChild(child: Node, cost: Int) {
        childrenCosts[child] = cost
    }

    fun getCost(child: Node): Int? {
        return childrenCosts[child]
    }

    fun getPathCost(): Int {
        var cost = 0
        var node: Node? = this
        while (node?.parent != null) {
            cost += node.parent!!.getCost(node)!!
            node = node.parent
        }
        return cost
    }

    override fun toString(): String {
        return if (gValue != null) {
            "$place(g:$gValue)"
        } else {
            place.toString()
        }
    }
}