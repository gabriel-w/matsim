package playground.mas.routing;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.population.Person;
import org.matsim.core.router.costcalculators.TravelDisutilityFactory;
import org.matsim.core.router.util.TravelDisutility;
import org.matsim.core.router.util.TravelTime;

import java.util.Collection;

public class MASCarTravelDisutilityFactory implements TravelDisutilityFactory {
    final private TravelDisutilityFactory carTravelDisutilityFactory;
    final private Collection<Id<Person>> evUserIds;
    final private MASCordonTravelDisutility cordonDisutility;

    public MASCarTravelDisutilityFactory(TravelDisutilityFactory carTravelDisutilityFactory, Collection<Id<Person>> evUserIds, MASCordonTravelDisutility cordonDisutility) {
        this.carTravelDisutilityFactory = carTravelDisutilityFactory;
        this.evUserIds = evUserIds;
        this.cordonDisutility = cordonDisutility;
    }

    @Override
    public TravelDisutility createTravelDisutility(TravelTime timeCalculator) {
        return new MASCarTravelDisutility(carTravelDisutilityFactory.createTravelDisutility(timeCalculator), evUserIds, cordonDisutility);
    }
}
