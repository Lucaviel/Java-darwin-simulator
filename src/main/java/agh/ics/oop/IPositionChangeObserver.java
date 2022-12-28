package agh.ics.oop;

public interface IPositionChangeObserver {

   void positionChanged(Animal animal, Vector2d oldPosition, Vector2d newPosition);
}

// get(klucz) -> wartosc
// put(klucz, wartosc)
// containsKey(klucz) -> szybko
// containsValue(wartosc) -> nie jest hashMapa wiec jest dluzej
// mapa nie jest iterable, .KeySet() -> Set<K> ; .entrySet() -> Set<Entry<klucz, wartosc>>
// values()-> Collection<V>