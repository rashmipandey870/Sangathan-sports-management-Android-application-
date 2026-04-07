package com.example.sangathanapp.ui.captain.network;

public class CaptainLoginResponse {

    private String message;
    private Team team;

    public String getMessage() { return message; }
    public Team getTeam() { return team; }
    public class Team{
        private String teamName;
        private String captainName;

        private String _id;
        public String getTeamName() { return teamName; }
        public String getCaptainName() { return captainName; }
        public String getId() { return _id; }
    }

}