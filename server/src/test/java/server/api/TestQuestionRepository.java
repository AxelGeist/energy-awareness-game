/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package server.api;

import commons.Question;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.QuestionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class TestQuestionRepository implements QuestionRepository {

    public final List<Question> questions = new ArrayList<>();
    public final List<String> calledMethods = new ArrayList<>();

    private void call(String name) {
        calledMethods.add(name);
    }


    @Override
    public List<Question> findAll() {
        calledMethods.add("findAll");
        return questions;

    }

    @Override
    public List<Question> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Question> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Question> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        return questions.size();
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(Question entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Question> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends Question> S save(S entity) {
        call("save");
        entity.setId(questions.size());
        questions.add(entity);
        return entity;

    }

    @Override
    public <S extends Question> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Question> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        call("existsById");
        for (Question q : questions) {
            if (q.getId() == aLong) return true;
        }
        return false;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Question> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Question> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Question> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Question getOne(Long aLong) {
        return null;
    }

    @Override
    public Question getById(Long id) {
        call("getById");
        return questions.stream().filter(q -> q.getId() == id).findFirst().get();

    }

    @Override
    public <S extends Question> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Question> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Question> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Question> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Question> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Question> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Question, R> R findBy(Example<S> example,
           Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}