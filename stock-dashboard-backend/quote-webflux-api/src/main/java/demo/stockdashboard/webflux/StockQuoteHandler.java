package demo.stockdashboard.webflux;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static org.springframework.web.reactive.function.server.ServerResponse.badRequest;
import static org.springframework.web.reactive.function.server.ServerResponse.notFound;
import demo.stockdashboard.utils.RandomUtil;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.SplittableRandom;
import java.util.stream.Stream;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import demo.stockdashboard.pojo.CompanyProfile;
import demo.stockdashboard.pojo.IntradayChartData;
import demo.stockdashboard.pojo.IntradayPrice;
import demo.stockdashboard.pojo.PreviousQuote;
import demo.stockdashboard.pojo.Quote;
import demo.stockdashboard.repo.CompanyProfileRepo;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class StockQuoteHandler {
	
	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private CompanyProfileRepo companyRepo;

//	public Mono<ServerResponse> company(ServerRequest request) {
//		return request.queryParam("symbol")
//				.map(symbol -> companyRepo.findBySymbol(symbol)
//							.map(profile -> ServerResponse
//									.ok()
//									.contentType(MediaType.APPLICATION_JSON)
//									.bodyValue(profile))
//							.orElse(ServerResponse
//									.notFound()
//									.build()))
//				.orElse(ServerResponse
//						.status(HttpStatus.BAD_REQUEST)
//						.bodyValue("parameter symbol is missing"));
//	}
	private Mono<ServerResponse> findBySymbol(String symbol) {
		return companyRepo.findBySymbol(symbol)
				.map(profile -> ok().contentType(MediaType.APPLICATION_JSON).bodyValue(profile))
				.orElse(notFound().build());
	}

	public Mono<ServerResponse> company(ServerRequest request) {
		return request.queryParam("symbol")
				.map(symbol -> findBySymbol(symbol))
				.orElse(badRequest().bodyValue("parameter symbol is missing"));
	}

	public Mono<ServerResponse> previous(ServerRequest request) {
		Optional<String> optSymbol = request.queryParam("symbol");
		Optional<String> optDate = request.queryParam("date");
		
		if (!optSymbol.isPresent()) {
			return ServerResponse
					.status(HttpStatus.BAD_REQUEST)
					.bodyValue("parameter symbol is missing");
		}
		
		if (!optDate.isPresent()) {
			return ServerResponse
					.status(HttpStatus.BAD_REQUEST)
					.bodyValue("parameter date is missing");
		}
		
		LocalDate date = null;
		if (optDate.isPresent()) {
			try {
				date = LocalDate.parse(optDate.get(),DateTimeFormatter.ISO_LOCAL_DATE);
			} catch (DateTimeParseException e) {
				return ServerResponse
						.status(HttpStatus.BAD_REQUEST)
						.bodyValue("invalid format for date parameter '" + optDate.get() + "', should be in yyyy-mm-dd format");				
			}
		}
		
		String symbol = optSymbol.get();
		
		Query query = query(where("symbol").regex("^"+symbol+"$", "i").and("date").is(date));
		
		PreviousQuote previous = mongoTemplate.findOne(query, PreviousQuote.class, "previous");
		if (previous!=null) {
			return ServerResponse
					.ok()
					.contentType(MediaType.APPLICATION_JSON)
					.bodyValue(previous);
		} else {
			return ServerResponse
					.notFound()
					.build();
		}
	
	}
	
	public Mono<ServerResponse> intradayInitDemo(ServerRequest request) {
		Optional<String> optSymbol = request.queryParam("symbol");
		if (!optSymbol.isPresent()) {
			return ServerResponse
					.status(HttpStatus.BAD_REQUEST)
					.bodyValue("parameter symbol is missing");
		}
		String symbol = optSymbol.get().toLowerCase();

		Optional<String> optDate = request.queryParam("date");
		LocalDate date = null;
		if (!optDate.isPresent()) {
			return ServerResponse
					.status(HttpStatus.BAD_REQUEST)
					.bodyValue("either date or since parameter should be provided");
		} else {
			try {
				date = LocalDate.parse(optDate.get(),DateTimeFormatter.ISO_LOCAL_DATE);
			} catch (DateTimeParseException e) {
				return ServerResponse
						.status(HttpStatus.BAD_REQUEST)
						.bodyValue("invalid format for date parameter '" + optDate.get() + "', should be in yyyy-mm-dd format");				
			}			
		}
		//
		boolean result = Stream.of(request.queryParam("symbol"), request.queryParam("date"))
				.map(v -> v.isPresent())
				.reduce((first, second) -> first && second)
				.orElse(false);
		if (!result) return badRequest().bodyValue("parameter symbol is missing");

//		Optional<String> optSymbol2 = request.queryParam("symbol")
//				.map(symbol2 -> symbol2.toLowerCase());
//		Optional<String> optDate2 = request.queryParam("date")
//				.map(date2 -> date2.toLowerCase());
//
//		Stream.of(optSymbol2, optDate2)
//						.map(v -> v.isPresent())
//				.reduce((first, second) -> first && second)
//				.orElse(false);
//		if (result) return badRequest().bodyValue("parameter symbol is missing");

		
		Query prevQuery = query(where("symbol").is(symbol).and("date").lt(date)).with(Sort.by(Order.desc("date"))).limit(1);
//		 PreviousQuote previous = mongoTemplate.findOne(prevQuery, PreviousQuote.class, "previous"); // FIXME: (MongoDB) "previous"
		PreviousQuote previous = getPrevious(symbol, date);
		if (previous==null) {
			return ServerResponse
					.status(HttpStatus.NOT_FOUND)
					.bodyValue("cannot find previous closing price for [" + symbol + "] on [" + date + "]");
		}

		LocalDateTime from = LocalDateTime.of(date, LocalTime.of(9,30));
		LocalTime now = LocalTime.now();
		int interval = (now.getMinute()*60 + now.getSecond())%600;
		long mins = (long)Math.ceil((390d/600d)*interval);
		LocalDateTime to = from.plusMinutes(mins);

		log.info("symbol='{}' : from='{}' - to='{}'", symbol, from, to);
		Query query = query(where("symbol").is(symbol).and("timestamp").gte(from).lte(to)).with(Sort.by(Order.asc("timestamp")));
//		List<IntradayPrice> prices = mongoTemplate.find(query, IntradayPrice.class, "intraday-prices");
//		IntradayChartData data = new IntradayChartData();
//		data.setSymbol(symbol);
//		data.setTradeDate(date);
//		data.setPrevious(previous);
//		data.setQuote(prices);
		return ServerResponse
				.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(new IntradayChartData(symbol,
						date,
						previous,
						mongoTemplate.find(query, IntradayPrice.class, "intraday-prices"))); // FIXME: (MongoDB) "intraday-prices"
	}

	public Mono<ServerResponse> intradayDeltaDemo(ServerRequest request) {
		Optional<String> optSymbol = request.queryParam("symbol");
		Optional<String> optSince = request.queryParam("since");
		
		if (!optSymbol.isPresent()) {
			return ServerResponse
					.status(HttpStatus.BAD_REQUEST)
					.bodyValue("parameter symbol is missing");
		}
		
		LocalDateTime since = null;
		if (!optSince.isPresent()) {
			return ServerResponse
					.status(HttpStatus.BAD_REQUEST)
					.bodyValue("since parameter should be provided");
		} else {
			try {
				since = LocalDateTime.parse(optSince.get(),DateTimeFormatter.ISO_DATE_TIME);
			} catch (DateTimeParseException e) {
				return ServerResponse
						.status(HttpStatus.BAD_REQUEST)
						.bodyValue("invalid format for since parameter '" + optSince.get() + "', should be in yyyy-mm-ddThh:mm:ss format");				
			}
		}
		
		String symbol = optSymbol.get().toLowerCase();
		
		LocalDateTime from = LocalDateTime.of(since.toLocalDate(), LocalTime.of(9,30));

		LocalTime now = LocalTime.now();
		int interval = (now.getMinute()*60 + now.getSecond())%600;
		long mins = (long)Math.ceil((390d/600d)*interval);
		LocalDateTime to = from.plusMinutes(mins);

		from = since;
		
//		log.info("symbol='{}' : from='{}' - to='{}'", symbol, from, to);
		Query query = query(where("symbol").is(symbol).and("timestamp").gt(from).lte(to)).with(Sort.by(Order.asc("timestamp")));
		List<IntradayPrice> intraday = mongoTemplate.find(query, IntradayPrice.class, "intraday-prices"); // FIXME: (MongoDB) "intraday-prices"
//		List<IntradayPrice> intraday = getIntraday(symbol, from, to);

//		long count = mongoTemplate.count(query, IntradayPrice.class, "intraday-prices");
//		log.info("count='{}' intraday-size='{}'", count, intraday.size());

		return ServerResponse
				.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(intraday);
	}

	private SplittableRandom seed = new SplittableRandom();
			
	public Mono<ServerResponse> realTimeQuoteDemo(ServerRequest request) {
		Optional<String> optSymbol = request.queryParam("symbol");
		Optional<String> optDate = request.queryParam("date");
		
		if (!optSymbol.isPresent()) {
			return ServerResponse
					.status(HttpStatus.BAD_REQUEST)
					.bodyValue("parameter symbol is missing");
		}
		
		if (!optDate.isPresent()) {
			return ServerResponse
					.status(HttpStatus.BAD_REQUEST)
					.bodyValue("parameter date is missing");
		}
		
		LocalDate date = null;
		if (optDate.isPresent()) {
			try {
				date = LocalDate.parse(optDate.get(),DateTimeFormatter.ISO_LOCAL_DATE);
			} catch (DateTimeParseException e) {
				return ServerResponse
						.status(HttpStatus.BAD_REQUEST)
						.bodyValue("invalid format for date parameter '" + optDate.get() + "', should be in yyyy-mm-dd format");				
			}
		}		
		
		String symbol = optSymbol.get();
		Query query = query(where("symbol").regex("^"+symbol+"$", "i").and("date").is(date));
		
		PreviousQuote previous = mongoTemplate.findOne(query, PreviousQuote.class, "previous");
		if (previous!=null) {
			Quote quote = new Quote();
			BeanUtils.copyProperties(previous, quote);
			
			float priceWithNoise = (float)(previous.getClose() * (1 + seed.nextDouble(-0.15d, 0.15d)));
			float prevClose = previous.getClose();
			float change = priceWithNoise-prevClose;
			
			quote.setLatestPrice(priceWithNoise);
			quote.setPreviousClose(prevClose);
			quote.setChange(change);
			quote.setChangePercent(change/prevClose*100);
			quote.setLatestUpdate(LocalDateTime.now());
			return ServerResponse
					.ok()
					.contentType(MediaType.APPLICATION_JSON)
					.bodyValue(quote);
		} else {
			return ServerResponse
					.notFound()
					.build();
		}	
	}

	/*
	 * Fix (MongoDB) "previous"
	 *      *******
	 * TODO:  PreviousQuote previous = mongoTemplate.findOne(prevQuery, PreviousQuote.class, "previous");
	 */
	private PreviousQuote getPrevious(String symbol, LocalDate date) {
		PreviousQuote previous = new PreviousQuote();
		previous.setId(org.bson.types.ObjectId.get());
		previous.setSymbol(symbol);
		previous.setDate(date);
		previous.setVolume(RandomUtil.getLong(10000l)); //previous.setVolume(100l);
		previous.setOpen(RandomUtil.getFloat(1f));      //previous.setOpen(1.0f);
		previous.setClose(RandomUtil.getFloat(1f));     //previous.setClose(0.0f);
		previous.setLow(-1f);
		previous.setHigh(1f);
		return previous;
	}

	/*
	 * Fix (MongoDB) "intraday-prices"
	 *      *******
	 * TODO:  List<IntradayPrice> intraday = mongoTemplate.find(query, IntradayPrice.class, "intraday-prices");
	 */
	private List<IntradayPrice> getIntraday(String symbol, LocalDateTime from, LocalDateTime to) { return getIntraday(symbol, from, to, 1); }
	private List<IntradayPrice> getIntraday(String symbol, LocalDateTime from, LocalDateTime to, int maxIntraday) {
		List<IntradayPrice> intraday = new ArrayList<>();
		for (int count=0; count<=maxIntraday; count++) {
			IntradayPrice price = new IntradayPrice();
			price.setId(org.bson.types.ObjectId.get());
			price.setSymbol(symbol);
			price.setTimestamp(to);
			price.setVolume(RandomUtil.getLong(10000l));
			price.setClose(RandomUtil.getFloat(1f));
			price.setOpen(RandomUtil.getFloat(1f));
			price.setLow(-1f);
			price.setHigh(1f);
			intraday.add(price);
		}
		return intraday;
	}
}
