package com.fuhu.demo.service;

import com.fuhu.demo.models.LocationStats;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class CoronaVirusDataService {
    private static String VIRUS_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_daily_reports/02-13-2021.csv";
    private List<LocationStats> allStats = new ArrayList<>();
    @PostConstruct
    public void fetchVirusData() throws IOException, InterruptedException {
        List<LocationStats> newStats = new ArrayList<>();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(VIRUS_DATA_URL))
                .build();
        HttpResponse<String> httpResponse = client.send(request,HttpResponse.BodyHandlers.ofString());
        StringReader csvBodyReader = new StringReader(httpResponse.body());
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);
        for (CSVRecord record:records) {
            String state = record.get("Province_State");
            //System.out.println(state);
            LocationStats locationStats = new LocationStats();
            locationStats.setCountry(record.get("Country_Region"));
            locationStats.setState(record.get("Combined_Key"));
            locationStats.setLatestTotalCase(Integer.parseInt(record.get("Confirmed")));
            newStats.add(locationStats);
            System.out.print("Country_Region: " + record.get("Country_Region") + "  Province_State: "
            + record.get("Combined_Key") + "  Confirmed: " + record.get("Confirmed"));
            System.out.println("");
        }
        this.allStats = newStats;
    }
}
