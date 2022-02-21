package nextstep.subway.applicaion;

import java.util.List;
import nextstep.subway.applicaion.dto.ShortestPathResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import nextstep.subway.domain.SubwayMap;
import nextstep.subway.error.exception.NotFoundStationException;
import org.springframework.stereotype.Service;

@Service
public class PathService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public PathService(LineRepository lineRepository,
            StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public ShortestPathResponse getShortestPath(Long sourceId, Long targetId) {
        List<Line> lines = lineRepository.findAll();
        Station source = stationRepository.findById(sourceId).orElseThrow(NotFoundStationException::new);
        Station target = stationRepository.findById(targetId).orElseThrow(NotFoundStationException::new);

        SubwayMap map = new SubwayMap(lines);

        List<Station> shortestPath = map.getShortestPath(source, target);
        double shortestDistance = map.getShortestDistance(source, target);

        return new ShortestPathResponse(shortestPath, shortestDistance);
    }
}