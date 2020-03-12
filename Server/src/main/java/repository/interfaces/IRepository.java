package repository.interfaces;

public interface IRepository<E,ID> {

    void add(E e);
    void update(E e);
    void delete(ID id);
    E findOne(ID id);
    Iterable<E> findAll();
}
