package demo.stockdashboard.repo;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import demo.stockdashboard.pojo.CompanyProfile;

@Repository
public interface CompanyProfileRepo extends MongoRepository<CompanyProfile, String> {

	Optional<CompanyProfile> findBySymbol(String symbol);
	
}
