import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class CruiseSearch {

	class Rate {
		String rateCode;
		String rateGroup;
		public Rate(String rateCode, String rateGroup) {
			this.rateCode = rateCode;
			this.rateGroup = rateGroup;
		}
		public String getRateCode() {
			return rateCode;
		}
		public void setRateCode(String rateCode) {
			this.rateCode = rateCode;
		}
		public String getRateGroup() {
			return rateGroup;
		}
		public void setRateGroup(String rateGroup) {
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
		public String getCabinCode() {
			return cabinCode;
		}
		public void setCabinCode(String cabinCode) {
			this.cabinCode = cabinCode;
		}
		public String getRateCode() {
			return rateCode;
		}
		public void setRateCode(String rateCode) {
			this.rateCode = rateCode;
		}
		public BigDecimal getPrice() {
			return price;
		}
		public void setPrice(BigDecimal price) {
			this.price = price;
		}
	}
	class BestGroupPrice implements Comparable<BestGroupPrice> {
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
		public String getCabinCode() {
			return cabinCode;
		}
		public void setCabinCode(String cabinCode) {
			this.cabinCode = cabinCode;
		}
		public String getRateCode() {
			return rateCode;
		}
		public void setRateCode(String rateCode) {
			this.rateCode = rateCode;
		}
		public BigDecimal getPrice() {
			return price;
		}
		public void setPrice(BigDecimal price) {
			this.price = price;
		}
		public String getRateGroup() {
			return rateGroup;
		}
		public void setRateGroup(String rateGroup) {
			this.rateGroup = rateGroup;
		}
		@Override
		public int compareTo(BestGroupPrice bgp) {
			return price.compareTo(bgp.price);
		}
	}
	
	
	public BestGroupPrice[] getBestGroupPricesJ8(Rate[] rates, CabinPrice[] prices) {
		Map<String, String> rateMap = Arrays.stream(rates)
				.collect(Collectors.toMap(Rate::getRateCode, Rate::getRateGroup));
	
		// enrich the price with rateGroup to facilitate sorting and filtering
		List<BestGroupPrice> allGrpPrices = Arrays.stream(prices)
				.map(cp->new BestGroupPrice(cp.cabinCode, cp.rateCode, cp.price, rateMap.get(cp.rateCode)))
				.collect(Collectors.toList());
		
		// create a set of unique rateGroups 
		Set<String> rateGrpSet = Arrays.stream(rates)
				.map(Rate::getRateGroup)
				.collect(Collectors.toSet());
		
		// set of unique cabin types
		Set<String> cabinCodeSet = Arrays.stream(prices)
				.map(CabinPrice::getCabinCode)
				.collect(Collectors.toSet());
		
		// I am sure this operation below could be reduced to more elegant functional solution
		List<BestGroupPrice> bestGrpPrices = new ArrayList<CruiseSearch.BestGroupPrice>();
		// filter out only one cabin code, and then find best group rate in there for each group
		cabinCodeSet.forEach(cabCode-> {
			rateGrpSet.forEach(rtGrp->{
				BestGroupPrice bestPrice = allGrpPrices.stream()
						.filter(gp->(gp.getCabinCode().equals(cabCode) && gp.getRateGroup().equals(rtGrp)))
						.sorted()      // this does the sorting in descending order based on BaseGrpPrice:Comparable impl
						.findFirst()
						.orElse(null); // Optional result is evaluated to null if none present
				bestGrpPrices.add(bestPrice);
			});
		});
		
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
		
		BestGroupPrice[] bests = s.getBestGroupPricesJ8(rates, prices);
		for (BestGroupPrice p : bests) {
			System.out.println(p.toString());
		}
	}
}
