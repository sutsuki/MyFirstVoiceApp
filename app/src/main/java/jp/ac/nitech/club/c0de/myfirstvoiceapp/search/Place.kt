package jp.ac.nitech.club.c0de.myfirstvoiceapp.search

/**
 *
 * @author t_kamiya
 * Created on 2018/10/05
 */
enum class Place(private val str: String) {
    L_A_AIRPORT("ロサンゼルス空港"),
    UCLA("カリフォルニア大学ロサンゼルス校"),
    HOLLYWOOD("ハリウッド"),
    ANAHEIM("アナハイム"),
    GRAND_CANYON("グランドキャニオン"),
    SAN_DIEGO("サンディエゴ"),
    DOWN_TOWN("ダウンタウン"),
    PASADENA("パサデナ"),
    DISNEY_LAND("ディズニーランド"),
    LAS_VEGAS("ラスベガス");

    fun toNode() = Node(this)

    override fun toString(): String {
        return str
    }
}