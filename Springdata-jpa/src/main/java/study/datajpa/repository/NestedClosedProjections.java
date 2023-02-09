package study.datajpa.repository;

public interface NestedClosedProjections {

//    중첩 구조에서는 첫번째 애는 정확하게 최적화가 되지만 두번째인 team부터는 최적화가 안된다
    String getUsername();
    TeamInfo getTeam();

    interface TeamInfo{
        String getName();
    }
}
