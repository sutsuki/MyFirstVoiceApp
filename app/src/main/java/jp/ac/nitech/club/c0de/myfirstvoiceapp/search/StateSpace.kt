package jp.ac.nitech.club.c0de.myfirstvoiceapp.search

/**
 *
 * @author t_kamiya
 * Created on 2018/10/04
 */
class StateSpace(from: Place, to: Place) {
    val nodes = Place.values().map { it.toNode() }.toTypedArray()
    val start: Node
    val goal: Node

    init {
        nodes[0].addChild(nodes[1], 1)
        nodes[0].addChild(nodes[2], 3)
        nodes[1].addChild(nodes[2], 1)
        nodes[1].addChild(nodes[6], 6)
        nodes[2].addChild(nodes[3], 6)
        nodes[2].addChild(nodes[6], 6)
        nodes[2].addChild(nodes[7], 3)
        nodes[3].addChild(nodes[4], 5)
        nodes[3].addChild(nodes[7], 2)
        nodes[3].addChild(nodes[8], 4)
        nodes[4].addChild(nodes[8], 2)
        nodes[4].addChild(nodes[9], 1)
        nodes[5].addChild(nodes[1], 1)
        nodes[6].addChild(nodes[5], 7)
        nodes[6].addChild(nodes[7], 2)
        nodes[7].addChild(nodes[8], 1)
        nodes[7].addChild(nodes[9], 7)
        nodes[8].addChild(nodes[9], 5)

        start = nodes[from.ordinal]
        goal = nodes[to.ordinal]
    }
}