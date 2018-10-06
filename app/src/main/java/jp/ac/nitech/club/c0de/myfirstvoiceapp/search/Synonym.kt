package jp.ac.nitech.club.c0de.myfirstvoiceapp.search

/**
 *
 * @author t_kamiya
 * Created on 2018/10/04
 */
object Synonym {
    val map: Map<String, Place>
    init {
        val tmp = mutableMapOf(
                "LA 空港" to Place.L_A_AIRPORT,
                "LA空港" to Place.L_A_AIRPORT,
                "空港" to Place.L_A_AIRPORT,
                "LA Airport" to Place.L_A_AIRPORT,
                "LAX" to Place.L_A_AIRPORT,

                "UCLA" to Place.UCLA,
                "カルフォルニア大学" to Place.UCLA,
                "カルフォルニア" to Place.UCLA,
                "大学" to Place.UCLA
        )
        for (place in Place.values()) {
            tmp[place.toString()] = place
        }
        map = tmp
    }
}