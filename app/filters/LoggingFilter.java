package filters;

import akka.stream.Materializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import play.Logger;
import play.api.mvc.RequestHeader;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Filter;
import play.mvc.Http;
import play.mvc.Result;
import utils.MyConstants.*;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public class LoggingFilter extends Filter {

    Materializer mat;

    @Inject
    public LoggingFilter(Materializer mat) {
        super(mat);
        this.mat = mat;
    }

    @Override
    public CompletionStage<Result> apply(Function<Http.RequestHeader, CompletionStage<Result>> nextFilter,
                                         Http.RequestHeader request) {

        long startTime = System.currentTimeMillis();
        return nextFilter.apply(request).thenApply(result -> {
            long endTime = System.currentTimeMillis();
            long requestTime = endTime - startTime;

            if (request.contentType().isPresent()) {
                ObjectNode requestJson = Json.newObject();
                requestJson.put("method", request.method());
                requestJson.put("content_type", request.contentType().get().toString());
                requestJson.put("host", request.host());
                requestJson.put("uri", request.uri());
                requestJson.put("ip", request.remoteAddress());
                requestJson.put("session_token", request.getHeader(ApiRequestHeaders.SESSION_TOKEN_HEADER));
                requestJson.set("headers", Json.toJson(request.headers()));

                JsonNode responseBody = null;

                if (request.contentType().get().equals("application/json") || request.contentType().get().equals("application/x-www-form-urlencoded") ||
                        request.contentType().get().equals("multipart/form-data")) {
                    akka.util.ByteString body = play.core.j.JavaResultExtractor.getBody(result, 10000L, mat);
                    responseBody = Json.parse(body.decodeString("UTF-8"));
                }

                ObjectNode responseJson = Json.newObject();
                responseJson.put("status_code", result.status());
                responseJson.set("response_data", responseBody);

                ObjectNode apiLogsJson = Json.newObject();
                apiLogsJson.put("start_time", startTime);
                apiLogsJson.put("end_time", endTime);
                apiLogsJson.put("time_taken", requestTime);
                apiLogsJson.set("request_params", requestJson);
                apiLogsJson.set("response_params", responseJson);

                if (request.contentType().get().equals("application/json")) {
                    requestJson.put("request_data", "resul");
                }

                if (!request.hasHeader("no-log")) {
                    Logger.info(apiLogsJson.toString());
                }
            }

            return result.withHeader("Request-Time", "" + requestTime);
        });
    }
}