package ru.v1as.model;

import com.google.common.base.Preconditions;
import org.joda.time.DateTime;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.User;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by ivlasishen
 * on 13.04.2017.
 */
public class Game implements GroupSession {

    public static final String NOT_IN_GAME = "Этот пользователь не в игре";
    private final String id;
    private final Chat chat;
    private DateTime mark;
    private GameState state = GameState.NOT_STARTED;
    private List<User> users = new ArrayList<>();
    private List<User> missionUsers = new ArrayList<>();
    private Map<User, Role> roles = new HashMap<>();
    private Integer missionFailed = 0;
    private Integer missionSucceeded = 0;
    private User leader;
    private int leaderChanged = -1;
    private Message gatheringMessage;
    private Map<Integer, MissionVote> voters;

    public Game(Chat chat) {
        this.id = UUID.randomUUID().toString();
        this.chat = chat;
        this.mark = new DateTime();
        this.voters = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public Chat getChat() {
        return chat;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public DateTime getMark() {
        return mark;
    }

    public void setMark(DateTime mark) {
        this.mark = mark;
    }

    public Map<User, Role> getRoles() {
        return roles;
    }

    public void setRoles(Map<User, Role> roles) {
        this.roles = roles;
    }

    public Integer getMissionFailed() {
        return missionFailed;
    }

    public void setMissionFailed(Integer missionFailed) {
        this.missionFailed = missionFailed;
    }

    public Integer getMissionSucceeded() {
        return missionSucceeded;
    }

    public void setMissionSucceeded(Integer missionSucceeded) {
        this.missionSucceeded = missionSucceeded;
    }

    public User getLeader() {
        return leader;
    }

    public void setLeader(User leader) {
        this.leader = leader;
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        System.out.println(String.format("Game %s have changed state to %s", id, state));
        this.state = state;
    }

    public boolean containsUser(User u1) {
        return users.stream().filter(u -> u1.getId().equals(u.getId())).findAny().orElse(null) != null;
    }

    public Long getChatId() {
        return chat.getId();
    }

    public String getChatTitle() {
        return this.chat.getTitle();
    }

    public List<User> getMissionUsers() {
        return missionUsers;
    }

    public Message getGatheringMessage() {
        return gatheringMessage;
    }

    public void setGatheringMessage(Message gatheringMessage) {
        this.gatheringMessage = gatheringMessage;
    }

    public int getMissionIndex() {
        return missionFailed + missionSucceeded;
    }

    public void addVoter(MissionVote missionVote) {
        this.voters.put(missionVote.getUser().getId(), missionVote);
    }

    public void clearVoter() {
        this.voters = new HashMap<>();
    }

    public MissionVote setVote(User user, Boolean vote) {
        MissionVote missionVote = this.voters.get(user.getId());
        Preconditions.checkNotNull(missionVote, NOT_IN_GAME);
        missionVote.setVote(vote);
        return missionVote;
    }

    public Collection<MissionVote> getVotes() {
        return this.voters.values();
    }

    public Integer usersAmount() {
        return this.users.size();
    }

    public Set<Integer> getMissionUserIds() {
        return this.missionUsers.stream().map(User::getId).collect(Collectors.toSet());
    }

    public Set<Integer> getUsersId() {
        return this.users.stream().map(User::getId).collect(Collectors.toSet());
    }

    public int getLeaderChanged() {
        return leaderChanged;
    }

    public void setLeaderChanged(int leaderChanged) {
        this.leaderChanged = leaderChanged;
    }

}
