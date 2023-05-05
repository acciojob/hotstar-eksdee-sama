package com.driver.services;


import com.driver.EntryDto.SubscriptionEntryDto;
import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.repository.SubscriptionRepository;
import com.driver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SubscriptionService {

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    UserRepository userRepository;

    public Integer buySubscription(SubscriptionEntryDto subscriptionEntryDto){

        //Save The subscription Object into the Db and return the total Amount that user has to pay
        User userTag = userRepository.findById(subscriptionEntryDto.getUserId()).get();
        Subscription sub = new Subscription();
        sub.setSubscriptionType(subscriptionEntryDto.getSubscriptionType());
        sub.setNoOfScreensSubscribed(subscriptionEntryDto.getNoOfScreensRequired());

        //Total amount calculate
        int amount=0;
        if(sub.getSubscriptionType().toString().equals("BASIC")) {
            amount = 500 + (300 * subscriptionEntryDto.getNoOfScreensRequired());
        }
        else if (sub.getSubscriptionType().toString().equals("PRO")) {
            amount = 800 + (350 * subscriptionEntryDto.getNoOfScreensRequired());
        }
        else{
            amount = 1000 + (450 * subscriptionEntryDto.getNoOfScreensRequired());
        }

        sub.setTotalAmountPaid(amount);
        sub.setUser(userTag);
        userTag.setSubscription(sub);

        userRepository.save(userTag);
        //subscriptionRepository.save(subscription);

        return amount;
    }

    public Integer upgradeSubscription(Integer userId)throws Exception{

        //If you are already at an ElITE subscription : then throw Exception ("Already the best Subscription")
        //In all other cases just try to upgrade the subscription and tell the difference of price that user has to pay
        //update the subscription in the repository

        User user = userRepository.findById(userId).get();
        Subscription userSubscription = user.getSubscription();

        if(userSubscription.getSubscriptionType().toString().equals("ELITE")){
            throw new Exception("Already the best Subscription");
        }

        int previousPaid = user.getSubscription().getTotalAmountPaid();
        int currentPaid = 0;

        if(userSubscription.getSubscriptionType().toString().equals("BASIC")){
            currentPaid = 800 + (250 * user.getSubscription().getNoOfScreensSubscribed());
            userSubscription.setSubscriptionType(SubscriptionType.PRO);
        }
        else{
            currentPaid = 1000 + (350 * user.getSubscription().getNoOfScreensSubscribed());
            userSubscription.setSubscriptionType(SubscriptionType.ELITE);
        }

        subscriptionRepository.save(userSubscription);

        return currentPaid-previousPaid;
    }

    public Integer calculateTotalRevenueOfHotstar(){

        //We need to find out total Revenue of hotstar : from all the subscriptions combined
        //Hint is to use findAll function from the SubscriptionDb

        List<Subscription> subscriptionList = subscriptionRepository.findAll();

        int revenue = 0;

        for(Subscription subscription : subscriptionList){
            revenue += subscription.getTotalAmountPaid();
        }

        return revenue;
    }

}
