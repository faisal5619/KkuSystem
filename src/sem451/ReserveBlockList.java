package sem451;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReserveBlockList {

    private List<ReserveBlock> reserveBlocks;

    public ReserveBlockList() {
        this.reserveBlocks = new ArrayList<>();
    }

    public void addReserveBlock(ReserveBlock reserveBlock) {
        this.reserveBlocks.add(reserveBlock);
    }

    public boolean removeReserveBlock(ReserveBlock reserveBlock) {
        return this.reserveBlocks.remove(reserveBlock);
    }

    public List<ReserveBlock> findReservedBlocksAt(LocalDate date) {
        return this.reserveBlocks.stream()
                                 .filter(rb -> rb.getDate().equals(date))
                                 .collect(Collectors.toList());
    }

    public List<ReserveBlock> getAllReserveBlocks() {
        return new ArrayList<>(this.reserveBlocks);
    }

    public List<ReserveBlock> findReservedBlocksByPerson(Person person) {
        return this.reserveBlocks.stream()
                                 .filter(rb -> rb.getBy().equals(person))
                                 .collect(Collectors.toList());
    }

    public List<ReserveBlock> findReservedBlocksByRoom(Room room) {
        return this.reserveBlocks.stream()
                                 .filter(rb -> rb.getRoom().equals(room))
                                 .collect(Collectors.toList());
    }

}
