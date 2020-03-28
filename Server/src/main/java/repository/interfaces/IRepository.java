package repository.interfaces;

public interface IRepository<E,ID> {

    void add(E e) throws Exception;
    void update(E e);
    void delete(ID id);
    E findOne(ID id) throws Exception;
    Iterable<E> findAll();
}
