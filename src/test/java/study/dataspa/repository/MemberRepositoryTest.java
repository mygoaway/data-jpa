package study.dataspa.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.dataspa.dto.MemberDto;
import study.dataspa.entity.Member;
import study.dataspa.entity.Team;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.print.attribute.standard.PageRanges;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(false)
public class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamRepository teamRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("테스트 1번")
    public void testName() throws Exception {
        System.out.println("memberRepository.getClass() = " + memberRepository.getClass());
        Member member = new Member("jay");
        Member saveMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(saveMember.getId()).get();

        assertThat(findMember.getId()).isEqualTo(saveMember.getId());
        assertThat(findMember.getUsername()).isEqualTo(saveMember.getUsername());
        assertThat(saveMember).isEqualTo(findMember);
    }

    @Test
    @DisplayName("테스트 2번")
    public void basicCRUD() {

        Member member1 = new Member("jay1");
        Member member2 = new Member("jay2");

        memberRepository.save(member1);
        memberRepository.save(member2);

        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();

        assertThat(member1).isEqualTo(findMember1);
        assertThat(member2).isEqualTo(findMember2);

        // 리스트 조회
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        Long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        memberRepository.delete(member1);
        memberRepository.delete(member2);

        Long deleteCount = memberRepository.count();
        assertThat(deleteCount).isEqualTo(0);
    }

    @Test
    @DisplayName("테스트 3번")
    public void findByUsernameAndAgeGreaterThen() {
        Member memberA = new Member("aaa", 10);
        Member memberB = new Member("aaa", 20);
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        List<Member> aaa = memberRepository.findByUsernameAndAgeGreaterThan("aaa", 15);
        assertThat(aaa.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("테스트 5번")
    public void findByUsername() {
        Member memberA = new Member("aaa", 10);
        Member memberB = new Member("aaa", 20);
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        List<Member> aaa = memberRepository.findByUsername("aaa");
        assertThat(aaa.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("테스트 6번")
    public void findByUser() {
        Member memberA = new Member("aaa", 10);
        Member memberB = new Member("aaa", 20);
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        List<Member> aaa = memberRepository.findUser("aaa", 10);
        assertThat(aaa.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("테스트 7번")
    public void findByUsernameList() {
        Member memberA = new Member("aaa", 10);
        Member memberB = new Member("bbb", 20);
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        List<String> aaa = memberRepository.findByUsernameList();
        assertThat(aaa.get(0)).isEqualTo("aaa");
        assertThat(aaa.get(1)).isEqualTo("bbb");
    }

    @Test
    @DisplayName("테스트 8번")
    public void findMemberDto() {
        Team team = new Team("teamA");
        teamRepository.save(team);

        Member memberA = new Member("aaa", 10);
        memberA.setTeam(team);
        memberRepository.save(memberA);


        List<MemberDto> memberDtoList = memberRepository.findMemberDto();
        assertThat(memberDtoList.get(0).getUsername()).isEqualTo("aaa");
        assertThat(memberDtoList.get(0).getTeamname()).isEqualTo("teamA");
    }

    @Test
    @DisplayName("테스트 9번")
    public void findByNames() {
        Member memberA = new Member("aaa", 10);
        memberRepository.save(memberA);

        Member memberB = new Member("bbb", 10);
        memberRepository.save(memberB);

        List<Member> memberDtoList = memberRepository.findByNames(Arrays.asList("aaa","bbb"));
        for (Member member : memberDtoList) {
            System.out.println("member.getUsername() = " + member.getUsername());
        }
    }

    @Test
    @DisplayName("테스트 10번")
    public void returnType() {
        Member memberA = new Member("aaa", 10);
        Member memberB = new Member("bbb", 10);
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        List<Member> listMember = memberRepository.findListByUsername("aaa");
        for (Member member : listMember) {
            System.out.println("member.getUsername() = " + member.getUsername());
        }

        Member member = memberRepository.findMemberByUsername("aaa");
        System.out.println("member.getUsername() = " + member.getUsername());

        Optional<Member> optionalMember = memberRepository.findOptionalByUsername("aaa");
        Member findMember = optionalMember.get();
        System.out.println("findMember.getUsername() = " + findMember.getUsername());
    }

    @Test
    @DisplayName("테스트 11번")
    public void paging() {
        Member member1 = new Member("member1", 10);
        Member member2 = new Member("member2", 10);
        Member member3 = new Member("member3", 10);
        Member member4 = new Member("member4", 10);
        Member member5 = new Member("member5", 10);

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);
        memberRepository.save(member5);

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        Page<Member> page = memberRepository.findPageByAge(age, pageRequest);
        List<Member> content = page.getContent();
        long totalCount = page.getTotalElements();

        /*
        for (Member member : content) {
            System.out.println("member = " + member);
        }
        System.out.println("totalCount = " + totalCount);
        */

        assertThat(content.size()).isEqualTo(3);
        assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();

        // 페이지 유지하면서 dto로 변환
        Page<MemberDto> toMap = page.map(member -> new MemberDto(member.getId(), member.getUsername(), null));
    }

    @Test
    @DisplayName("테스트 12번")
    public void pagingSlice() {
        Member member1 = new Member("member1", 10);
        Member member2 = new Member("member2", 10);
        Member member3 = new Member("member3", 10);
        Member member4 = new Member("member4", 10);
        Member member5 = new Member("member5", 10);

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);
        memberRepository.save(member5);

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        Slice<Member> slice = memberRepository.findSliceByAge(age, pageRequest);
        List<Member> content = slice.getContent();

        assertThat(content.size()).isEqualTo(3);
        assertThat(slice.getNumber()).isEqualTo(0);
        assertThat(slice.isFirst()).isTrue();
        assertThat(slice.hasNext()).isTrue();
    }

    @Test
    @DisplayName("테스트 13번")
    public void list() {
        Member member1 = new Member("member1", 10);
        Member member2 = new Member("member2", 10);
        Member member3 = new Member("member3", 10);
        Member member4 = new Member("member4", 10);
        Member member5 = new Member("member5", 10);

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);
        memberRepository.save(member5);

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        List<Member> content = memberRepository.findListByAge(age, pageRequest);
        assertThat(content.size()).isEqualTo(3);
    }


    @Test
    @DisplayName("테스트 14번")
    public void bulkAgePlus() {
        Member member1 = new Member("member1", 10);
        Member member2 = new Member("member2", 19);
        Member member3 = new Member("member3", 20);
        Member member4 = new Member("member4", 21);
        Member member5 = new Member("member5", 40);

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);
        memberRepository.save(member5);

        int age = 20;
        int resultCount = memberRepository.bulkAgePlus(age);
        // em.flush();
        // em.clear();

        List<Member> findMember = memberRepository.findByUsername("member5");
        Member member = findMember.get(0);
        System.out.println("member.getAge() = " + member.getAge());

        assertThat(resultCount).isEqualTo(3);
    }

    @Test
    @DisplayName("테스트 15번")
    public void findMemberLazy() {

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        // 영속성 컨택스트에 있는 내용을 DB 반영 후 초기화 작업
        em.flush();
        em.clear();

        // List<Member> members = memberRepository.findAll();
        // List<Member> members = memberRepository.findMemberFetchJoin();
        // List<Member> members = memberRepository.findMemberEntityGraph();
        List<Member> members = memberRepository.findEntityGraphByUsername("member2");
        for (Member member : members) {
            System.out.println("member = " + member.getUsername());
            System.out.println("member.getTeam().getClass() = " + member.getTeam().getClass());
            System.out.println("member.getTeam().getName() = " + member.getTeam().getName());
        }
    }

    @Test
    public void queryHint() {
        // given
        Member member = new Member("member1", 10);
        memberRepository.save(member);
        em.flush();
        em.clear();

        // when
        Member findMember = memberRepository.findReadOnlyByUsername("member1");
        findMember.setUsername("member2");
        em.flush(); // 상태가 바꼇다는걸 인지(dirty checking)
    }

    @Test
    public void lock() {
        // given
        Member member = new Member("member1", 10);
        memberRepository.save(member);
        em.flush();
        em.clear();

        // when
        List<Member> findMember = memberRepository.findLockByUsername("member1");
    }
}
