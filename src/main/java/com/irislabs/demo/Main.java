package com.irislabs.demo;

import com.irislabs.sheet.FileSheet;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Author: spartango
 * Date: 4/15/14
 * Time: 12:03 AM.
 */
public class Main {

    public static void main(String[] args) throws IOException {
        FileSheet sheet = new FileSheet("test.tsv");

//      SheetWriter writer = new SheetWriter(System.currentTimeMillis() + ".tsv", sheet.fields());

        System.out.println("Headers: " + sheet.fields());
//      Headers: [npi, nppes_provider_last_org_name, nppes_provider_first_name, nppes_provider_mi, nppes_credentials, nppes_provider_gender, nppes_entity_code, nppes_provider_street1, nppes_provider_street2, nppes_provider_city, nppes_provider_zip, nppes_provider_state, nppes_provider_country, provider_type, medicare_participation_indicator, place_of_service, hcpcs_code, hcpcs_description, line_srvc_cnt, bene_unique_cnt, bene_day_srvc_cnt, average_Medicare_allowed_amt, stdev_Medicare_allowed_amt, average_submitted_chrg_amt, stdev_submitted_chrg_amt, average_Medicare_payment_amt, stdev_Medicare_payment_amt]

        final List<String> targets = Arrays.asList();

        sheet.stream()
             .filter(entry -> entry.get("provider_type").equals("Clinical Laboratory"))
             .filter(entry -> targets.stream()
                                     .filter(name -> entry.get("hcpcs_code").startsWith(name))
                                     .findAny().isPresent())
             .forEach(entry -> System.out.println(entry.get("hcpcs_code")
                                                  + ": "
                                                  + entry.get("nppes_provider_last_org_name")));

//        final List<String> targets = Arrays.asList("GENOMIC HEALTH", "MYRIAD", "VERACYTE", "COUNSYL");

//        sheet.stream()
//             .filter(entry -> targets.stream()
//                                     .filter(name -> entry.get("nppes_provider_last_org_name").contains(name))
//                                     .findAny().isPresent())
//             .collect(MultiCollectors.groupingByKey("nppes_provider_last_org_name", Collectors.toList()))
//             .entrySet()
//             .forEach(org -> {
//                 System.out.println(org.getKey());
//                 org.getValue()
//                    .stream()
//                    .forEach(entry -> System.out.println(
//                            entry.get("hcpcs_code")
//                            + ": "
//                            + entry.get("line_srvc_cnt")
//                            + "x / "
//                            + entry.get("bene_unique_cnt")
//                            + " p, $"
//                            + entry.get("average_submitted_chrg_amt")
//                            + " / $"
//                            + entry.get("average_Medicare_allowed_amt")
//                            + " / $"
//                            + entry.get("average_Medicare_payment_amt")));
//             });

//        sheet.stream()
//             .filter(entry -> keys.stream()
//                                  .map(entry::containsKey)
//                                  .filter(result -> result)
//                                  .count() == keys.size())
//             .filter(entry -> entry.get("provider_type").equals("Clinical Laboratory"))
//             .collect(MultiCollectors.groupingByKey("nppes_provider_last_org_name",
//                                                    MultiCollectors.groupingByKey("hcpcs_code",
//                                                                                  Collectors.summingDouble(
//                                                                                          entry -> entry.getDouble(
//                                                                                                  "line_srvc_cnt")
//                                                                                                   *
//                                                                                                   entry.getDouble(
//                                                                                                           "average_Medicare_payment_amt")))))
//             .entrySet()
//             .stream()
//             .sorted(Comparator.comparingDouble(group -> group.getValue()
//                                                              .values()
//                                                              .stream()
//                                                              .mapToDouble(entry -> entry)
//                                                              .sum()))
//             .forEach(group -> {
//                          System.out.print(group.getKey() + ": [");
//                          group.getValue()
//                               .entrySet()
//                               .stream()
//                               .sorted(Comparator.comparingDouble(Map.Entry::getValue))
//                               .forEach(entry -> System.out.print("(" + entry.getKey()
//                                                                  + ": "
//                                                                  + entry.getValue()
//                                                                  + "), "));
//                          System.out.println("]");
//                      }
//             );

//        sheet.stream()
//             .filter(entry -> keys.stream()
//                                  .map(entry::containsKey)
//                                  .filter(result -> result)
//                                  .count() == keys.size())
//             .collect(MultiCollectors.groupingByKey("hcpcs_code",
//                                                    MultiCollectors.descriptiveSummarizing(
//                                                            entry -> entry.getDouble(
//                                                                    "line_srvc_cnt") *
//                                                                     entry.getDouble(
//                                                                             "average_Medicare_payment_amt"))))
//             .entrySet()
//             .stream()
//             .sorted(Comparator.comparing(group -> group.getValue().getSum()));

//        writer.close();
    }
}
