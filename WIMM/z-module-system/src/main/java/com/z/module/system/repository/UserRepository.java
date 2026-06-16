package com.z.module.system.repository;

import com.z.module.system.domain.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the {@link User} entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    String USERS_BY_LOGIN_CACHE = "usersByLogin";

    @Cacheable(cacheNames = USERS_BY_LOGIN_CACHE)
    Optional<User> findOneByLogin(String login);

    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesByLogin(String login);

    void deleteByLogin(String login);

    void deleteAllByIdIn(List<Long> idList);

    Optional<User> findOneByPhoneNumber(String phoneNumber);

    /**
     * 统计某家庭下的用户数, 用于判断删除用户后该家庭是否已清空.
     */
    long countByTenantId(String tenantId);

    /**
     * 按id批量查询用户(含 tenant_id), 用于删除前收集受影响的家庭号.
     */
    List<User> findAllByIdIn(List<Long> idList);
}
