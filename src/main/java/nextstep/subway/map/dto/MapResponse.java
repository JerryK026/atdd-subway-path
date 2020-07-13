package nextstep.subway.map.dto;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.dto.LineResponse;

import java.util.List;

public class MapResponse {
    private List<LineResponse> data;

    public static MapResponse of(List<LineResponse> lines) {
        return new MapResponse(lines);
    }

    public List<LineResponse> getData() {
        return data;
    }

    public MapResponse(List<LineResponse> data) {
        this.data = data;
    }

    public MapResponse() {
    }
}
