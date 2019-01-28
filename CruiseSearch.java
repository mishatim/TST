import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CruiseSearch {

	class Rate {
		String rateCode;
		String rateGroup;
		public Rate(String rateCode, String rateGroup) {
			this.rateCode = rateCode;
			this.rateGroup = rateGroup;
		}
	}
	class CabinPrice {
		String cabinCode;
		String rateCode;
		BigDecimal price;
		public CabinPrice(String cabCd, String rateCd, BigDecimal prc) {
			this.cabinCode = cabCd;
			this.rateCode = rateCd;
			this.price = prc;
		}
	}
	class BestGroupPrice {
		String cabinCode;
		String rateCode;
		BigDecimal price;
		String rateGroup;
		public BestGroupPrice(String cabCd, String rateCd, BigDecimal prc, String rateGrp) {
			this.cabinCode = cabCd;
			this.rateCode = rateCd;
			this.price = prc;
			this.rateGroup = rateGrp;
		}
		public String toString() {
			return (String.format("BestGroupPrice %s %s %f %s", cabinCode, rateCode, price, rateGroup));   
		}
	}
	
	public BestGroupPrice[] getBestGroupPrices(Rate[] rates, CabinPrice[] prices) {
		HashMap<String, String> rateMap = new HashMap<String, String>();
		for (Rate r : rates) {
			rateMap.put(r.rateCode, r.rateGroup);
		}
		
		// enrich the price with rateGroup to facilitate sorting and filtering
		List<BestGroupPrice> allGrpPrices = new ArrayList<CruiseSearch.BestGroupPrice>();
		for (CabinPrice cp : prices) {
			allGrpPrices.add(new BestGroupPrice(cp.cabinCode, cp.rateCode, cp.price, rateMap.get(cp.rateCode)));
		}
		
		// create a set of unique rateGroups 
		Set<String> rateGrpSet = new HashSet<String>();
		for (Rate r : rates) {
			rateGrpSet.add(r.rateGroup);
		}
		
		// set of unique cabin types
		Set<String> cabinCodeSet = new HashSet<String>();
		for (CabinPrice cp : prices) {
			cabinCodeSet.add(cp.cabinCode);
		}
		
		
		List<BestGroupPrice> bestGrpPrices = new ArrayList<CruiseSearch.BestGroupPrice>();
		// filter out only one cabin code, and then find best group rate in there for each group
		for (String cabCd : cabinCodeSet) {
			for(String rtGrp : rateGrpSet) {
				BestGroupPrice bestPrice = null;
				for (BestGroupPrice gp : allGrpPrices) {
					if (gp.cabinCode.equals(cabCd) && gp.rateGroup.equals(rtGrp)) {
						// check best price
						if (bestPrice==null || gp.price.compareTo(bestPrice.price) < 0) {
							bestPrice = gp;
						}
					}
				}
				bestGrpPrices.add(bestPrice);
			}
		}
		
		
		return bestGrpPrices.toArray(new BestGroupPrice[] {});
	}
	
	public static void main(String[] args) {
		CruiseSearch s = new CruiseSearch();
		
		Rate[] rates = new Rate[] {
				s.new Rate("M1", "Military"),
				s.new Rate("M2", "Military"),
				s.new Rate("S1", "Senior"),
				s.new Rate("S2", "Senior")
		};
		CabinPrice[] prices = new CabinPrice[] {
				s.new CabinPrice("CA", "M1", new BigDecimal("200.00")),
				s.new CabinPrice("CA", "M2", new BigDecimal("250.00")),
				s.new CabinPrice("CA", "S1", new BigDecimal("225.00")),
				s.new CabinPrice("CA", "S2", new BigDecimal("260.00")),
				s.new CabinPrice("CB", "M1", new BigDecimal("230.00")),
				s.new CabinPrice("CB", "M2", new BigDecimal("260.00")),
				s.new CabinPrice("CB", "S1", new BigDecimal("245.00")),
				s.new CabinPrice("CB", "S2", new BigDecimal("270.00"))
		};
		
		BestGroupPrice[] bests = s.getBestGroupPrices(rates, prices);
		for (BestGroupPrice p : bests) {
			System.out.println(p.toString());
		}
	}
}
