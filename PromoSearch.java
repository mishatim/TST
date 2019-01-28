import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class PromoSearch {

	class Promotion {
		String code;
		String[] notCombinableWith;
		public Promotion(String code, String[] notCombinableWith) {
			this.code = code;
			this.notCombinableWith = notCombinableWith;
		}
		@Override
		public boolean equals(Object obj) {
			return this.code.equals(((Promotion)obj).code);
		}
		@Override
		public int hashCode() {
			return 31 * code.hashCode() + notCombinableWith.hashCode();
		}
	}
	class PromotionCombo {
		String[] promotionCodes;
		public PromotionCombo(String[] promotionCodes) {
			this.promotionCodes = promotionCodes;
		}
	}
	
	public PromotionCombo[] allCombinablePromotions(Promotion[] allPromos) {
		List<PromotionCombo> prComboLst = new ArrayList<>();
		
		List<List<String>> promoCombos = new ArrayList<>();
		for (int i=0; i<allPromos.length; i++) {
			List<String> combos = findCombos(i, allPromos);
			Collections.sort(combos);
			if (!promoCombos.contains(combos)) {
				promoCombos.add(combos);
				prComboLst.add(new PromotionCombo(combos.toArray(new String[] {})));
			}
		}
				 
		return prComboLst.toArray(new PromotionCombo[]{});
	}
	
	List<String> findCombos(int pos, Promotion[] promos) {
		List<String> compCodes = new ArrayList<>();
		List<String> nonCompLst = new ArrayList<>();
		for (int i=0; i<promos.length; i++) {
			if (i==pos) continue;
			Promotion p = promos[i];
			nonCompLst.addAll(Arrays.asList(p.notCombinableWith));
			if (!nonCompLst.contains(p.code)) {
				compCodes.add(p.code);
			}
		}
		return compCodes;
	}
	
	public static void main(String[] args) {
		PromoSearch s = new PromoSearch();
		
		Promotion[] promoArr = new Promotion[] {
			s.new Promotion("P1", new String[]{"P3"}), // P1 is not combinable with P3
			s.new Promotion("P2", new String[]{"P4", "P5"}), // P2 is not combinable with P4 and P5
			s.new Promotion("P3", new String[]{"P1"}), // P3 is not combinable with P1
			s.new Promotion("P4", new String[]{"P2"}), // P4 is not combinable with P2
			s.new Promotion("P5", new String[]{"P2"}) // P5 is not combinable with P2
		};
		
		s.allCombinablePromotions(promoArr);
	}
}
