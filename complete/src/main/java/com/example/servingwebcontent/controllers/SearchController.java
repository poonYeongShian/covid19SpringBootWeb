package com.example.servingwebcontent.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.example.servingwebcontent.models.api.APIfactory;
import com.example.servingwebcontent.models.api.Get;
import com.example.servingwebcontent.models.api.TestingSiteFactory;
import com.example.servingwebcontent.models.apimodel.TestingSite;
import com.example.servingwebcontent.domain.BrowseForm;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SearchController {

    public List<TestingSite> getTestingSiteList() {
        List<TestingSite> testingSiteList = new ArrayList<>();

        APIfactory<TestingSite> testingSiteFactory = new TestingSiteFactory(System.getenv("API_KEY"));
        Get<TestingSite> testingSiteGet = testingSiteFactory.createGet();

        try {
            // Testing-site collection
            Collection<TestingSite> testingSiteCollection = testingSiteGet.getApi();

            for (TestingSite testingSite : testingSiteCollection) {
                testingSiteList.add(testingSite);
            }

        } catch (Exception e) {
            System.out.println(e);
        }

        return testingSiteList;
    }

    @GetMapping("/browse")
    public String browseTestingSite(Model model) {

        List<TestingSite> testingSiteList = getTestingSiteList();

        model.addAttribute("testingSiteList", testingSiteList);

        return "testing-site/browse";
    }

    @PostMapping("/search")
    public String searchTestingSite(@ModelAttribute("browseForm") BrowseForm browseForm, Model model) {
        APIfactory<TestingSite> testingSiteFactory = new TestingSiteFactory(System.getenv("API_KEY"));
        Get<TestingSite> testingSiteGet = testingSiteFactory.createGet();

        List<TestingSite> filteredTestingSiteList = new ArrayList<>();

        try {
            // Testing-site collection
            Collection<TestingSite> testingSiteCollection = testingSiteGet.getApi();

            String suburbName = browseForm.getSuburbName().toLowerCase();

            // Search for a substring in a string
            for (TestingSite testingSite : testingSiteCollection) {
                // Filter the testing site based on suburb name
                if (testingSite.getName().toLowerCase().contains(suburbName)) {
                    filteredTestingSiteList.add(testingSite);
                }
            }

        } catch (Exception e) {
            System.out.println(e);
        }

        model.addAttribute("testingSiteList", filteredTestingSiteList);

        return "testing-site/browse";
    }

    @PostMapping("/select")
    public String filterByTypeOfFacility(@ModelAttribute("browseForm") BrowseForm browseForm, Model model) {
        APIfactory<TestingSite> testingSiteFactory = new TestingSiteFactory(System.getenv("API_KEY"));

        Get<TestingSite> testingSiteGet = testingSiteFactory.createGet();

        List<TestingSite> filteredTestingSiteList = new ArrayList<>();

        String typeOfFacility = browseForm.getTypeOfFacility().toLowerCase();

        try {
            // Testing-site collection
            Collection<TestingSite> testingSiteCollection = testingSiteGet.getApi();

            // Search for a substring in a string
            for (TestingSite testingSite : testingSiteCollection) {
                // Filter the testing site based on type of facility
                if (testingSite.getAdditonalInfo().getTypeOfFacility().toLowerCase().contains(typeOfFacility)) {
                    filteredTestingSiteList.add(testingSite);
                }
            }

        } catch (Exception e) {
            System.out.println(e);
        }

        model.addAttribute("testingSiteList", filteredTestingSiteList);

        return "testing-site/browse";
    }

    @RequestMapping("/show/{id}")
    public String showTestingSite(@PathVariable String id, Model model)
            throws IOException, InterruptedException {

        List<TestingSite> testingSiteList = getTestingSiteList();

        for (TestingSite testingSite : testingSiteList) {
            // Get the testing site if it matches the ID
            if (id.equals(testingSite.getId())) {
                model.addAttribute("testingSite", testingSite);
                break;
            }
        }

        return "testing-site/show";
    }
}
