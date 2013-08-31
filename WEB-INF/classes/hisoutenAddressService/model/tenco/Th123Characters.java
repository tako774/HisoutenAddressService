package hisoutenAddressService.model.tenco;

/**
 * 
 * @author bngper
 * 
 */
public class Th123Characters {

	/**
	 * 霊夢
	 */
	public static final Th123Characters REIMU = new Th123Characters(0);
	/**
	 * 魔理沙
	 */
	public static final Th123Characters MARISA = new Th123Characters(1);
	/**
	 * 咲夜
	 */
	public static final Th123Characters SAKUYA = new Th123Characters(2);
	/**
	 * アリス
	 */
	public static final Th123Characters ALICE = new Th123Characters(3);
	/**
	 * パチュリー
	 */
	public static final Th123Characters PATCHOULI = new Th123Characters(4);
	/**
	 * 妖夢
	 */
	public static final Th123Characters YOUMU = new Th123Characters(5);
	/**
	 * レミリア
	 */
	public static final Th123Characters REMILIA = new Th123Characters(6);
	/**
	 * 幽々子
	 */
	public static final Th123Characters YUYUKO = new Th123Characters(7);
	/**
	 * 紫
	 */
	public static final Th123Characters YUKARI = new Th123Characters(8);
	/**
	 * 萃香
	 */
	public static final Th123Characters SUIKA = new Th123Characters(9);
	/**
	 * 鈴仙
	 */
	public static final Th123Characters REISEN = new Th123Characters(10);
	/**
	 * 文
	 */
	public static final Th123Characters AYA = new Th123Characters(11);
	/**
	 * 小町
	 */
	public static final Th123Characters KOMACHI = new Th123Characters(12);
	/**
	 * 衣玖
	 */
	public static final Th123Characters IKU = new Th123Characters(13);
	/**
	 * 天子
	 */
	public static final Th123Characters TENSHI = new Th123Characters(14);
	/**
	 * 早苗
	 */
	public static final Th123Characters SANAE = new Th123Characters(15);
	/**
	 * チルノ
	 */
	public static final Th123Characters CIRNO = new Th123Characters(16);
	/**
	 * 美鈴
	 */
	public static final Th123Characters MEIRIN = new Th123Characters(17);
	/**
	 * 空
	 */
	public static final Th123Characters UTSUHO = new Th123Characters(18);
	/**
	 * 諏訪子
	 */
	public static final Th123Characters SUWAKO = new Th123Characters(19);
	public final int Value;

	@Deprecated
	private Th123Characters() {
		this(0);
	}

	private Th123Characters(int value) {
		Value = value;
	}

	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof Th123Characters)) {
			return false;
		}
		if (other == this) {
			return true;
		}

		Th123Characters th123Character = (Th123Characters) other;

		return Value == th123Character.Value;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 41 * hash + this.Value;
		return hash;
	}
}
