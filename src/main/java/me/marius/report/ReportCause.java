package me.marius.report;

import java.util.stream.Stream;

public enum ReportCause {

    Hacking, Teaming, Bugusing, Boosting, Name, Skin, Clan, Trolling, Hunting;

    public static String getReportCause(String cause){

        for(ReportCause rc : values()){
            if(cause.equalsIgnoreCase(rc.toString()))
                return rc.toString();
        }
        return null;
    }

    public static Stream<ReportCause> stream(){
        return Stream.of(ReportCause.values());
    }

}
