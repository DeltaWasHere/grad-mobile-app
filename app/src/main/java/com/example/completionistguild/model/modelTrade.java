package com.example.completionistguild.model;

import com.example.completionistguild.exclusion.strategy1Exclude;
import com.example.completionistguild.exclusion.strategy2Exclude;

public class modelTrade {
    //basic offer infoz
    public String tradeId;
    public String avatar;
    public float tradeRate;
    public String userName;
    public Integer restriction;
    public String gameId;
    public Integer date;
    public String interestedgameId1;
    public String interestedGameId2;
    public String interestedgameId3;
    public modelGameInfo[] interestedGames;
    public String front;
    public String name;
    public String media;

    @strategy1Exclude
    public String acceptedId;

    @strategy1Exclude
    public Integer destinedId;

    @strategy1Exclude
    public String tradeNumbers;

    @strategy1Exclude
    @strategy2Exclude
    public String gameKey;

    @strategy1Exclude
    public String offerId;


    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public float getTradeRate() {
        return tradeRate;
    }

    public void setTradeRate(float tradeRate) {
        this.tradeRate = tradeRate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getRestriction() {
        return restriction;
    }

    public void setRestriction(Integer restriction) {
        this.restriction = restriction;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public Integer getDate() {
        return date;
    }

    public void setDate(Integer date) {
        this.date = date;
    }

    public String getInterestedgameId1() {
        return interestedgameId1;
    }

    public void setInterestedgameId1(String interestedgameId1) {
        this.interestedgameId1 = interestedgameId1;
    }

    public String getInterestedGameId2() {
        return interestedGameId2;
    }

    public void setInterestedGameId2(String interestedGameId2) {
        this.interestedGameId2 = interestedGameId2;
    }

    public String getInterestedgameId3() {
        return interestedgameId3;
    }

    public void setInterestedgameId3(String interestedgameId3) {
        this.interestedgameId3 = interestedgameId3;
    }

    public modelGameInfo[] getInterestedGames() {
        return interestedGames;
    }

    public void setInterestedGames(modelGameInfo[] interestedGames) {
        this.interestedGames = interestedGames;
    }

    public String getFront() {
        return front;
    }

    public void setFront(String front) {
        this.front = front;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAcceptedId() {
        return acceptedId;
    }

    public void setAcceptedId(String acceptedId) {
        this.acceptedId = acceptedId;
    }

    public Integer getDestinedId() {
        return destinedId;
    }

    public void setDestinedId(Integer destinedId) {
        this.destinedId = destinedId;
    }

    public String getTradeNumbers() {
        return tradeNumbers;
    }

    public void setTradeNumbers(String tradeNumbers) {
        this.tradeNumbers = tradeNumbers;
    }

    public String getGameKey() {
        return gameKey;
    }

    public void setGameKey(String gameKey) {
        this.gameKey = gameKey;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public modelTrade(String tradeId, String avatar, float tradeRate, String userName, Integer restriction, String gameId, Integer date, String interestedgameId1, String interestedGameId2, String interestedgameId3, modelGameInfo[] interestedGames, String front, String name, String media, String acceptedId, Integer destinedId, String tradeNumbers, String gameKey, String offerId) {
        this.tradeId = tradeId;
        this.avatar = avatar;
        this.tradeRate = tradeRate;
        this.userName = userName;
        this.restriction = restriction;
        this.gameId = gameId;
        this.date = date;
        this.interestedgameId1 = interestedgameId1;
        this.interestedGameId2 = interestedGameId2;
        this.interestedgameId3 = interestedgameId3;
        this.interestedGames = interestedGames;
        this.front = front;
        this.name = name;
        this.media = media;
        this.acceptedId = acceptedId;
        this.destinedId = destinedId;
        this.tradeNumbers = tradeNumbers;
        this.gameKey = gameKey;
        this.offerId = offerId;
    }
}
