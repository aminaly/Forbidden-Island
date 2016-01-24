import java.util.Iterator;

// class for iterating through IList
class IListIterator<T> implements Iterator<T> {

    // list field
    IList<T> list;

    // constructor
    IListIterator(IList<T> list) {
        this.list = list;
    }

    // this has a next if it is not empty
    public boolean hasNext() {
        return !this.list.isEmpty();
    }

    // return the next thing in the list
    public T next() {
        T returnValue = ((Cons<T>) this.list).first;
        this.list = ((Cons<T>) this.list).rest;
        return returnValue;
    }

    // don't do anything i suppose
    public void remove() {
        // this method is not supposed to do anything but must 
        // be implemented
    }

}