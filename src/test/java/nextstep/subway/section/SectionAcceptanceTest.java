package nextstep.subway.section;

import static common.Constants.강남역;
import static common.Constants.양재역;
import static common.Constants.빨강색600;
import static common.Constants.광교역;
import static common.Constants.신논현역;
import static common.Constants.신분당선;
import static common.Constants.판교역;
import static nextstep.subway.line.LineTestStepDefinition.지하철_노선_생성_요청;
import static nextstep.subway.line.LineTestStepDefinition.지하철_노선_조회_요청;
import static nextstep.subway.section.SectionTestStepDefinition.*;
import static org.assertj.core.api.Assertions.assertThat;
import static nextstep.subway.station.StationTestStepDefinition.지하철_역_생성_요청;

import common.AcceptanceTest;
import java.util.List;
import java.util.stream.Stream;
import nextstep.subway.station.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import nextstep.subway.line.LineResponse;

@DisplayName("지하철 구간 관련 기능 인수 테스트")
@AcceptanceTest
public class SectionAcceptanceTest {
//    private Long 판교역정보;
//    private Long 광교역정보;
//    private Long 양재역정보;
//    private Long
//
//    @BeforeEach
//    public void init() {
//
//    }

    // Given 지하철 노선을 생성하고
    // When 지하철 노선 구간 사이에 존재하는 구간을 추가하면
    // Then 지하철 노선 조회시 추가한 구간이 중간에 포함되어있다.
    @DisplayName("지하철 노선 사이에 존재하는 구간을 추가한다.")
    @Test
    void createSection_pass_insert() {
        // TODO: 역의 id를 받기 위해 역을 생성하고 주입하는 과정이 반복적인데, 이 반복되는 부분을 어떻게 추출할 수 있을까?
        // given
        var 판교역응답 = 지하철_역_생성_요청(판교역);
        var 광교역응답 = 지하철_역_생성_요청(광교역);
        var 신분당선응답 = 지하철_노선_생성_요청(신분당선, 빨강색600, 판교역응답.getId(), 광교역응답.getId(), 10);
        var 양재역응답 = 지하철_역_생성_요청(양재역);

        // when답
        지하철_구간_생성_요청(신분당선응답.getId(), 판교역응답.getId(), 양재역응답.getId(), 5);

        // then
        var 노선조회응답 = 지하철_노선_조회_요청(신분당선응답.getId());
        assertThat(getStationNames(노선조회응답)).containsExactly(판교역, 양재역, 광교역);
    }

    // Given 지하철 노선을 생성하고
    // When 지하철 노선 상행 종점역을 하행역으로 가지는 구간을 추가하면
    // Then 지하철 노선 조회시 추가한 구간이 맨 앞에 포함되어있다.
    @DisplayName("지하철 노선 사이에 존재하는 구간을 추가한다.")
    @Test
    void createSection_pass_addHead() {
        // given
        var 판교역응답 = 지하철_역_생성_요청(판교역);
        var 광교역응답 = 지하철_역_생성_요청(광교역);
        var 신분당선응답 = 지하철_노선_생성_요청(신분당선, 빨강색600, 판교역응답.getId(), 광교역응답.getId(), 10);
        var 양재역응답 = 지하철_역_생성_요청(양재역);

        // when
        지하철_구간_생성_요청(신분당선응답.getId(), 양재역응답.getId(), 판교역응답.getId(), 5);

        // then
        var 노선조회응답 = 지하철_노선_조회_요청(신분당선응답.getId());
        assertThat(getStationNames(노선조회응답)).containsExactly(양재역, 판교역, 광교역);
    }

    // Given 지하철 노선을 생성하고
    // When 지하철 노선 하행 종점역을 상행역으로 가지는 구간을 추가하면
    // Then 지하철 노선 조회시 추가한 구간이 맨 뒤에 포함되어있다.
    @DisplayName("지하철 노선 사이에 존재하는 구간을 추가한다.")
    @Test
    void createSection_pass_addTail() {
        // given
        var 판교역응답 = 지하철_역_생성_요청(판교역);
        var 광교역응답 = 지하철_역_생성_요청(광교역);
        var 신분당선응답 = 지하철_노선_생성_요청(신분당선, 빨강색600, 판교역응답.getId(), 광교역응답.getId(), 10);
        var 양재역응답 = 지하철_역_생성_요청(양재역);

        // when
        지하철_구간_생성_요청(신분당선응답.getId(), 광교역응답.getId(), 양재역응답.getId(), 5);

        // then
        var 노선조회응답 = 지하철_노선_조회_요청(신분당선응답.getId());
        assertThat(getStationNames(노선조회응답)).containsExactly(판교역, 광교역, 양재역);
    }

    // Given 지하철 노선을 생성하고
    // When 기존 구간과 거리가 같은 구간을 추가하면
    // Then 구간 추가에 실패한다
    @DisplayName("지하철 노선에 기존 구간과 거리가 같거나 큰 구간을 역 사이에 추가하면 실패한다.")
    @Test
    void createSection_fail_sectionDistanceIsSame() {
        // given
        var 강남역응답 = 지하철_역_생성_요청(강남역);
        var 판교역응답 = 지하철_역_생성_요청(판교역);
        var 신분당선응답 = 지하철_노선_생성_요청(신분당선, 빨강색600, 강남역응답.getId(), 판교역응답.getId(), 10);
        var 신논현역응답 = 지하철_역_생성_요청(신논현역);

        // when
        var 상태코드 = 지하철_구간_생성_요청_상태_코드_반환(신분당선응답.getId(),
            강남역응답.getId(), 신논현역응답.getId(), 10);

        // then
        assertThat(상태코드).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    // Given 지하철 노선을 생성하고
    // When 기존 구간의 상행역과 하행역이 같은 구간을 추가하면
    // Then 구간 추가에 실패한다
    @DisplayName("이미 등록된 상행역과 하행역을 가진 구간을 추가하면 실패한다")
    @Test
    void createSection_fail_sectionAlreadyExist() {
        // given
        var 강남역응답 = 지하철_역_생성_요청(강남역);
        var 판교역응답 = 지하철_역_생성_요청(판교역);
        var 신분당선응답 = 지하철_노선_생성_요청(신분당선, 빨강색600, 강남역응답.getId(), 판교역응답.getId(), 10);

        // when
        var 상태코드 = 지하철_구간_생성_요청_상태_코드_반환(신분당선응답.getId(), 강남역응답.getId(), 판교역응답.getId(), 10);

        // then
        assertThat(상태코드).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    // Given 지하철 노선을 생성하고
    // When 상행역과 하행역 모두 등록되지 않은 구간을 추가하면
    // Then 구간 추가에 실패한다
    @DisplayName("상행역과 하행역 모두 등록되지 않은 구간을 추가하면 실패한다")
    @Test
    void createSection_fail_sectionNoIntersection() {
        var 강남역응답 = 지하철_역_생성_요청(강남역);
        var 판교역응답 = 지하철_역_생성_요청(판교역);
        var 신분당선응답 = 지하철_노선_생성_요청(신분당선, 빨강색600, 강남역응답.getId(), 판교역응답.getId(), 10);

        var 상태코드 = 지하철_구간_생성_요청_상태_코드_반환(신분당선응답.getId(), 신논현역, 양재역, 10);

        assertThat(상태코드).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    // Given 지하철 노선에 구간을 추가하고
    // When 하행 종점역이 존재하는 구간을 제거 요청하면
    // Then 지하철 노선 조회시 추가한 구간이 제거된 것을 확인할 수 있다.
    @DisplayName("지하철 노선의 하행 종점역에 대한 구간을 제거한다.")
    @Test
    void deleteSection() {
        // given
        var lineCreateResponse = 지하철_노선_생성_요청(신분당선, 빨강색600, 판교역, 광교역, 10);
        var stationResponse = 지하철_역_생성_요청(양재역);
        지하철_구간_생성_요청(
            lineCreateResponse.getId(),
            getDownEndStationId(lineCreateResponse),
            stationResponse.getId(),
            10);

        // when
        지하철_구간_제거_요청(lineCreateResponse.getId(), stationResponse.getId());

        // then
        var lineResponse = 지하철_노선_조회_요청(lineCreateResponse.getId());
        assertThat(getStationNames(lineResponse)).containsExactly(판교역, 광교역);
    }

    // Given 지하철 노선에 구간을 생성하고
    // When 상행 요청역에 대해 삭제 요청하면
    // Then 지하철 노선 조회시 상행 종점역을 발견할 수 없다
    @DisplayName("지하철 노선의 상행 종점역에 대한 구간을 제거한다.")
    @Test
    void deleteSection_deleteUpEnd() {
        // given
        var 판교역_생성 = 지하철_역_생성_요청(판교역);
        var 광교역_생성 = 지하철_역_생성_요청(광교역);
        var 신분당선_생성 = 지하철_노선_생성_요청(신분당선, 빨강색600, 판교역_생성.getId(), 광교역_생성.getId(), 10);
        var 양재역_생성 = 지하철_역_생성_요청(양재역);
        지하철_구간_생성_요청(신분당선_생성.getId(), 광교역_생성.getId(), 양재역_생성.getId(), 10);

        // when
        지하철_구간_제거_요청(신분당선_생성.getId(), 판교역_생성.getId());

        // then
        var 노선_조회 = 지하철_노선_조회_요청(신분당선_생성.getId());
        assertThat(getStationNames(노선_조회)).containsExactly(광교역, 양재역);
    }

    // Given 지하철 노선에 구간을 생성하고
    // When 중간 역에 대한 삭제 요청하면
    // Then 지하철 노선 조회시 삭제한 구간이 제거되고, 양 옆의 구간이 연결되어 있는 것을 확인할 수 있다.
    @DisplayName("지하철 구간의 중간 역에 대해 제거한다")
    @Test
    void deleteSection_deleteMiddle() {
        var 판교역_생성 = 지하철_역_생성_요청(판교역);
        var 광교역_생성 = 지하철_역_생성_요청(광교역);
        var 신분당선_생성 = 지하철_노선_생성_요청(신분당선, 빨강색600, 판교역_생성.getId(), 광교역_생성.getId(), 10);
        var 양재역_생성 = 지하철_역_생성_요청(양재역);
        지하철_구간_생성_요청(신분당선_생성.getId(), 광교역_생성.getId(), 양재역_생성.getId(), 10);

        지하철_구간_제거_요청(신분당선_생성.getId(), 광교역_생성.getId());

        var 노선_조회_응답 = 지하철_노선_조회_요청(신분당선_생성.getId());
        assertThat(getStationNames(노선_조회_응답)).containsExactly(판교역, 양재역);
    }

    // Given 지하철 노선을 생성하고
    // When 상행 종점역과 하행 종점역만 있는 노선에 구간 삭제 요청하면
    // Then 지하철 노선 조회시 추가한 구역이 제거되지 않은 것을 확인할 수 있다.
    @DisplayName("지하철 노선에 구간 제거시 상행 종점역과 하행 종점역만 있는 노선에 구간이라면 실패한다.")
    @Test
    void deleteSection_fail_sectionOnlyExistsUpAndDownEndStationInLine() {
        // given
        var lineCreateResponse = 지하철_노선_생성_요청(신분당선, 빨강색600, 판교역, 광교역, 10);

        // when
        var statusCode = 지하철_구간_제거_요청_상태_코드_반환(lineCreateResponse.getId(),
            getUpEndStationId(lineCreateResponse));

        // then
        var lineResponse = 지하철_노선_조회_요청(lineCreateResponse.getId());
        assertThat(statusCode).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(getStationNames(lineResponse)).containsExactly(판교역, 광교역);
    }

    // Given 지하철 노선과 구간을 추가하고
    // When 상행종점역부터 하행종점역으로 경로 조회 요청하면
    // Then 출발역부터 도착역까지의 역 목록과 조회한 경로 구간의 거리를 확인할 수 있다
    @DisplayName("상행종점역부터 하행종점역 경로 조회 요청시 역 목록과 경로 구간의 거리를 확인할 수 있다.")
    @Test
    void route_upEnd2downEnd() {
        var 판교역_생성 = 지하철_역_생성_요청(판교역);
        var 광교역_생성 = 지하철_역_생성_요청(광교역);
        var 신분당선_생성 = 지하철_노선_생성_요청(신분당선, 빨강색600, 판교역_생성.getId(), 광교역_생성.getId(), 10);
        var 양재역_생성 = 지하철_역_생성_요청(양재역);
        지하철_구간_생성_요청(신분당선_생성.getId(), 광교역_생성.getId(), 양재역_생성.getId(), 10);

        var 경로_조회 = 지하철_경로_조회(판교역_생성.getId(), 양재역_생성.getId());

        assertThat(역이름_목록(경로_조회)).containsExactly(판교역, 광교역, 양재역);
        assertThat(경로_조회.getDistance()).isEqualTo(20);
    }

    // Given 지하철 노선과 구간을 추가하고
    // When 상행종점역부터 중간역으로 경로 조회 요청하면
    // Then 출발역부터 중간역까지의 역 목록과 조회한 경로 구간의 거리를 확인할 수 있다
    @DisplayName("상행종점역부터 중간역 경로 조회 요청시 역 목록과 경로 구간의 거리를 확인할 수 있다.")
    @Test
    void route_upEnd2Middle() {
        var 판교역_생성 = 지하철_역_생성_요청(판교역);
        var 광교역_생성 = 지하철_역_생성_요청(광교역);
        var 신분당선_생성 = 지하철_노선_생성_요청(신분당선, 빨강색600, 판교역_생성.getId(), 광교역_생성.getId(), 10);
        var 양재역_생성 = 지하철_역_생성_요청(양재역);
        지하철_구간_생성_요청(신분당선_생성.getId(), 광교역_생성.getId(), 양재역_생성.getId(), 10);

        var 경로_조회 = 지하철_경로_조회(판교역_생성.getId(), 광교역_생성.getId());

        assertThat(역이름_목록(경로_조회)).containsExactly(판교역, 광교역);
        assertThat(경로_조회.getDistance()).isEqualTo(10);
    }

    private Stream<String> 역이름_목록(PathResponse response) {
        return response.getStations().stream().map(StationResponse::getName);
    }

    private Stream<String> getStationNames(LineResponse response) {
        return response.getStations().stream().map(StationResponse::getName);
    }

    private Long getDownEndStationId(LineResponse response) {
        List<StationResponse> stations = response.getStations();
        return stations.get(stations.size() - 1).getId();
    }

    private Long getUpEndStationId(LineResponse response) {
        return response.getStations().get(0).getId();
    }
}
