package com.example.servingwebcontent.observer;

import com.example.servingwebcontent.models.api.APIfactory;
import com.example.servingwebcontent.models.api.Get;
import com.example.servingwebcontent.models.api.TestingSiteFactory;
import com.example.servingwebcontent.models.apimodel.AuthenticateSingleton;
import com.example.servingwebcontent.models.apimodel.Booking;
import com.example.servingwebcontent.models.apimodel.TestingSite;
import com.example.servingwebcontent.models.apimodel.User;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

public class BookingData implements Subject{
    private List<Booking> bookings;
    private ArrayList<Observer> observerList;
    private AuthenticateSingleton authenticateInstance = AuthenticateSingleton.getInstance();

    public BookingData() {
        observerList = new ArrayList<Observer>();
    }
    @Override
    public void registerObserver(Observer o) {
        observerList.add(o);
    }

    @Override
    public void unregisterObserver(Observer o) {
        observerList.remove(observerList.indexOf(o));
    }

    @Override
    public void notifyObservers()
    {
        for (Iterator<Observer> it = observerList.iterator(); it.hasNext();)
        {
            Observer o = it.next();
            o.update(bookings);
        }
    }

    private List<Booking>  getLatestBooking() throws InterruptedException, java.text.ParseException, ParseException, IOException {

        return checkDifferenceTime(getBookingListUsingTestingSite());

    }

    public List<Booking> getBookingListUsingTestingSite()
            throws InterruptedException, ParseException, IOException, java.text.ParseException {
        String api = System.getenv("API_KEY");
        APIfactory<TestingSite> factory = new TestingSiteFactory(api);
        Get<TestingSite> testingSiteGet = factory.createGet();
        Collection<TestingSite> testingSites = testingSiteGet.getApi();
        List<Booking> bookings = new ArrayList<>();
        // TO DO SEARCH FOR TESTING SITE WHICH THAT ADMIN RESPONSIBLE
        User user = authenticateInstance.getUser();
        for (TestingSite testingSite : testingSites) {
            for (Booking booking : testingSite.getBookings()) {
                if (checkPreviousTestSite(booking)) {
                    bookings.add(booking);
                }
            }
            if (testingSite.getId().equals(user.getTestingSiteId())) {
                bookings.addAll(testingSite.getBookings());
            }
        }
        return bookings;
    }

    public List<Booking> checkDifferenceTime(List<Booking> bookings) throws java.text.ParseException {
        // check the time difference of time and local time
        // see whether it is smaller than the set range in second
        int timeRangeAllowed = 60; // In 1 hrs
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String currTime = LocalDateTime.now().toString();
        Date currentTime = formatter.parse(currTime);
        List<Booking> bookings1 = new ArrayList<>();
        for (Booking booking : bookings) {
            if (!booking.getRecentUpdateTime().equals("")) {
                Date recentTime = formatter.parse(booking.getRecentUpdateTime());
                if ((currentTime.getTime() - recentTime.getTime()) / 1000 < timeRangeAllowed
                        || checkPreviousTestSite(booking)) {
                    bookings1.add(booking);
                }
            }
        }
        return bookings1;
    }

    public boolean checkPreviousTestSite(Booking booking) {
        boolean flag = false;
        User user = authenticateInstance.getUser();
        if (booking.getPreviousTestSite().equals(user.getTestingSiteId())) {
            flag = true;
        }
        return flag;
    }

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
    public List<Booking> dataChanged() throws InterruptedException, ParseException, java.text.ParseException, IOException {
        //get latest data
        bookings = getLatestBooking();
        notifyObservers();
        return bookings;
    }

}
