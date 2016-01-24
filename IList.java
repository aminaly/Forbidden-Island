import java.util.Iterator;

// IList interface (is iterable)
interface IList<T> extends Iterable<T> {

    // is this IList empty?
    boolean isEmpty();
}

// empty class
class Empty<T> implements IList<T> {

    // return an IList iterator containing this
    public Iterator<T> iterator() {
        return new IListIterator<T>(this);
    }

    // yeah this is empty
    public boolean isEmpty() {
        return true;
    }

}

// cons class
class Cons<T> implements IList<T> {

    // first and rest fields
    T first;
    IList<T> rest;

    // constructor
    Cons(T first, IList<T> rest) {
        this.first = first;
        this.rest = rest;
    }

    // return an IListIterator containing this
    public Iterator<T> iterator() {
        return new IListIterator<T>(this);
    }

    // no this is not empty
    public boolean isEmpty() {
        return false;
    }
}
