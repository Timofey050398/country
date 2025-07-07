package guru.qa.country.service;

import com.google.protobuf.Empty;
import guru.qa.country.*;
import guru.qa.country.domain.Country;
import guru.qa.country.domain.CountryGql;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class GrpcCountryService extends CountryServiceGrpc.CountryServiceImplBase {

    private final CountryService countryService;

    public GrpcCountryService(CountryService countryService) {
        this.countryService = countryService;
    }

    @Override
    public void countries(Empty request, StreamObserver<CountryListResponse> responseObserver) {
        var countries = countryService.allGqlCountries();
        var response =  CountryListResponse.newBuilder()
                .addAllCountries(
                        countries.stream()
                                .map(GrpcCountryService::toGrpc)
                                .toList()
                )
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void addCountry(CountryRequest request, StreamObserver<CountryResponse> responseObserver) {
        if (request.getCountryCode().isBlank() || request.getCountryName().isBlank()) {
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("countryCode and countryName must not be blank")
                    .asRuntimeException());
            return;
        }

        final CountryGql countryGql = countryService.addGqlCountry(
                new Country(request.getCountryCode(), request.getCountryName())
        );
        responseObserver.onNext(toGrpc(countryGql));
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<CountryRequest> addCountries(StreamObserver<CountResponse> responseObserver) {
        return new StreamObserver<>() {
            private final AtomicInteger counter = new AtomicInteger(0);
            @Override
            public void onNext(CountryRequest request) {
                try {
                    if (request.getCountryCode().isBlank() || request.getCountryName().isBlank()) {
                        throw new IllegalArgumentException("Blank fields");
                    }
                    countryService.addGqlCountry(
                            new Country(request.getCountryCode(), request.getCountryName())
                    );
                    counter.incrementAndGet();
                } catch (Exception e) {
                    System.err.println("Error adding country: " + e.getMessage());
                }
            }

            @Override
            public void onError(Throwable t) {
                System.err.println("Error in client stream: " + t.getMessage());
                responseObserver.onError(t);
            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(
                        CountResponse.newBuilder()
                                .setCount(counter.get())
                                .build()
                );
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public void editCountry(CountryRequest request, StreamObserver<CountryResponse> responseObserver) {
        if (request.getCountryCode().isBlank() || request.getCountryName().isBlank()) {
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("countryCode and countryName must not be blank")
                    .asRuntimeException());
            return;
        }
        CountryGql country = countryService.editGqlCountryName(request.getCountryCode(), request.getCountryName());
        responseObserver.onNext(toGrpc(country));
        responseObserver.onCompleted();
    }

    private static CountryResponse toGrpc(CountryGql gql){
        return CountryResponse.newBuilder()
                .setId(gql.id().toString())
                .setCountryName(gql.countryName())
                .setCountryCode(gql.countryCode())
                .build();
    }
}
