package hisoutenAddressService.model.tenco;

/**
 * 
 * @author bngper
 * 
 */
public class TencoRating {

	public final Th123Characters Character;
	public final int Value;
	public final int Deviation;

	@SuppressWarnings("unused")
	@Deprecated
	private TencoRating() {
		this(Th123Characters.REIMU, 1500, 0);
	}

	public TencoRating(Th123Characters character, int value, int deviation) {
		Character = character;
		Value = value;
		Deviation = deviation;
	}

	public boolean isMatch(int ratingValue, int span) {
		if (Value == ratingValue) {
			return true;
		}

		if (Value < ratingValue) {
			return ratingValue < Value + span;
		} else {
			return Value < ratingValue + span;
		}
	}

	public TencoRating copy() {
		return new TencoRating(Character, Value, Deviation);
	}
}
