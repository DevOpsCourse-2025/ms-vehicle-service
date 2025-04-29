package com.chiops.vehicle.libs.exceptions.hanlder;

import com.chiops.vehicle.libs.exceptions.entities.ErrorResponse;
import com.chiops.vehicle.libs.exceptions.exception.*;


import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import io.micronaut.http.server.exceptions.InternalServerException;
import jakarta.inject.Singleton;


@Produces
@Singleton
@Requires(classes = {ExceptionHandler.class})
public class GlobalExceptionHandler implements ExceptionHandler<RuntimeException, HttpResponse<ErrorResponse>> {

    @Override
    public HttpResponse<ErrorResponse> handle(HttpRequest request, RuntimeException exception) {
        HttpStatus status = determineHttpStatus(exception);
        ErrorResponse error = new ErrorResponse(
            status,
            exception.getMessage(),
            request.getPath()
        );
        return HttpResponse.status(status).body(error);
    }

    private HttpStatus determineHttpStatus(RuntimeException ex) {
        if (ex instanceof NotFoundException) return HttpStatus.NOT_FOUND;
        if (ex instanceof ConflictException) return HttpStatus.CONFLICT;
        if (ex instanceof BadRequestException || ex instanceof ParseError) 
            return HttpStatus.BAD_REQUEST;
        if (ex instanceof AuthenticationFailedException || ex instanceof NotAuthenticatedException) 
            return HttpStatus.UNAUTHORIZED;
        if (ex instanceof PermissionDeniedException) return HttpStatus.FORBIDDEN;
        if (ex instanceof MethodNotAllowedException) return HttpStatus.METHOD_NOT_ALLOWED;
        if (ex instanceof UnsupportedMediaTypeException) return HttpStatus.UNSUPPORTED_MEDIA_TYPE;
        if (ex instanceof InternalServerException); return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}