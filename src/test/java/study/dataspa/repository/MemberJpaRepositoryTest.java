package study.dataspa.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.dataspa.entity.Member;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberJpaRepositoryTest {

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Test
    @DisplayName("테스트 1번")
    public void testName() throws Exception {
        Member member = new Member("jay");
        Member saveMember = memberJpaRepository.save(member);

        Member findMember = memberJpaRepository.find(saveMember.getId());

        assertThat(findMember.getId()).isEqualTo(saveMember.getId());
        assertThat(findMember.getUsername()).isEqualTo(saveMember.getUsername());
        assertThat(saveMember).isEqualTo(findMember);
    }

    @Test
    @DisplayName("테스트 2번")
    public void basicCRUD() {

        Member member1 = new Member("jay1");
        Member member2 = new Member("jay2");

        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        Member findMember1 = memberJpaRepository.findById(member1.getId()).get();
        Member findMember2 = memberJpaRepository.findById(member2.getId()).get();

        assertThat(member1).isEqualTo(findMember1);
        assertThat(member2).isEqualTo(findMember2);

        // 리스트 조회
        List<Member> all = memberJpaRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        Long count = memberJpaRepository.count();
        assertThat(count).isEqualTo(2);

        memberJpaRepository.delete(member1);
        memberJpaRepository.delete(member2);

        Long deleteCount = memberJpaRepository.count();
        assertThat(deleteCount).isEqualTo(0);
    }

    @Test
    @DisplayName("테스트 3번")
    public void findByUsernameAndAgeGreaterThen() {
        Member memberA = new Member("aaa", 10);
        Member memberB = new Member("aaa", 20);
        memberJpaRepository.save(memberA);
        memberJpaRepository.save(memberB);

        List<Member> aaa = memberJpaRepository.findByUsernameAndAgeGreaterThen("aaa", 15);
        assertThat(aaa.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("테스트 4번")
    public void findByUsername() {
        Member memberA = new Member("aaa", 10);
        Member memberB = new Member("aaa", 20);
        memberJpaRepository.save(memberA);
        memberJpaRepository.save(memberB);

        List<Member> aaa = memberJpaRepository.findByUsername("aaa");
        assertThat(aaa.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("테스트 5번")
    public void paging() {
        Member member1 = new Member("member1", 10);
        Member member2 = new Member("member2", 20);
        Member member3 = new Member("member3", 30);
        Member member4 = new Member("member4", 40);
        Member member5 = new Member("member5", 50);

        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);
        memberJpaRepository.save(member3);
        memberJpaRepository.save(member4);
        memberJpaRepository.save(member5);

        int age = 10;
        int offset = 0;
        int limit = 3;

        List<Member> members = memberJpaRepository.findByPage(age, offset, limit);
        long totalCount = memberJpaRepository.totalCount(age);

        assertThat(members.size()).isEqualTo(totalCount);
    }

    @Test
    @DisplayName("테스트 6번")
    public void bulkAgePlus() {
        Member member1 = new Member("member1", 10);
        Member member2 = new Member("member2", 19);
        Member member3 = new Member("member3", 20);
        Member member4 = new Member("member4", 21);
        Member member5 = new Member("member5", 40);

        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);
        memberJpaRepository.save(member3);
        memberJpaRepository.save(member4);
        memberJpaRepository.save(member5);

        int age = 20;
        int resultCount = memberJpaRepository.bulkAgePlus(age);

        assertThat(resultCount).isEqualTo(3);
    }
}