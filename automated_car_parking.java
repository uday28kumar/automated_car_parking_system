import java.util.*;
import java.lang.*;
import java.io.*;

/* Car Class*/
class Car {
  private String regNo;
  private String color;

  public Car() {

  }

  public Car(String regNo, String color) {
    this.regNo = regNo;
    this.color = color;
  }

  public String getRegNo() {
    return this.regNo;
  }

  public String getColor() {
    return this.color;
  }
}

class AutomatedCarParker {
  private int totalNumberOfSlots;
  private int vsSize = 0;
  private int[] vacantSlots;
  private Car[] occupiedSlots;

  private void initilizeVacantSlots() {
    vacantSlots = new int[totalNumberOfSlots];
    for (int i = 0; i < this.totalNumberOfSlots; i++) {
      updateVacantSlots(i);
    }
    minHeap();
  }

  private void initilizeOccupiedSlots() {
    occupiedSlots = new Car[totalNumberOfSlots];
    for (int i = 0; i < totalNumberOfSlots; i++) {
      occupiedSlots[i] = null;
    }
  }

  private void minHeap() {
    for (int i = ((vsSize - 1) / 2); i >= 0; i--) {
      minHeapify(i);
    }
  }

  private boolean isLeaf(int pos) {
    if (pos >= (vsSize - 1) / 2 && pos <= vsSize - 1) {
      return true;
    }
    return false;
  }

  private void swap(int fpos, int spos) {
    int tmp;
    tmp = vacantSlots[fpos];
    vacantSlots[fpos] = vacantSlots[spos];
    vacantSlots[spos] = tmp;
  }

  private void minHeapify(int pos) {
    if (pos >= vsSize)
      return;
    if (!isLeaf(pos)) {
      if (vacantSlots[pos] > vacantSlots[(2 * pos) + 1] || vacantSlots[pos] > vacantSlots[(2 * pos) + 2]) {
        if (vacantSlots[(2 * pos) + 1] < vacantSlots[(2 * pos) + 2]) {
          swap(pos, (2 * pos) + 1);
          minHeapify((2 * pos) + 1);
        } else {
          swap(pos, (2 * pos) + 2);
          minHeapify((2 * pos) + 2);
        }
      }
    }
  }

  private int getFirstVacantSlot() {
    if (this.vsSize == 0) {
      return -1;
    }
    int slotNumber = vacantSlots[0];
    vacantSlots[0] = vacantSlots[vsSize - 1];
    vsSize--;
    minHeapify(0);
    return slotNumber;
  }

  public void updateOccupiedSlots(int slot, Car car) {
    if (slot == -1)
      return;

    if (car == null)
      updateVacantSlots(slot);
    occupiedSlots[slot] = car;
  }

  public int allotSlot(Car car) {
    int allotedSlot = getFirstVacantSlot();
    updateOccupiedSlots(allotedSlot, car);
    return allotedSlot;
  }

  public void createSlot(int n) {
    totalNumberOfSlots = n;
    initilizeVacantSlots();
    initilizeOccupiedSlots();
  }

  public void updateVacantSlots(int element) {
    if (vsSize >= totalNumberOfSlots) {
      return;
    }
    vacantSlots[vsSize] = element;
    int current = vsSize;

    while (vacantSlots[current] < vacantSlots[(current - 1) / 2]) {
      swap(current, (current - 1) / 2);
      current = (current - 1) / 2;
    }
    vsSize++;
  }

  public void displayStatus() {
    System.out.println("Slot No\tRegestration No\tColor");
    for (int i = 0; i < totalNumberOfSlots; i++) {
      if (occupiedSlots[i] != null) {
        System.out.println(i + 1 + "\t" + occupiedSlots[i].getRegNo() + "\t" + occupiedSlots[i].getColor());
      }
    }
  }

  public ArrayList<String> getCarsOfColor(String color) {
    ArrayList<String> res = new ArrayList<>();
    for (int i = 0; i < totalNumberOfSlots; i++) {
      if (occupiedSlots[i] != null && occupiedSlots[i].getColor().equalsIgnoreCase(color)) {
        res.add(occupiedSlots[i].getRegNo());
      }
    }
    return res;
  }

  public int getSlotNumberOfCar(String regNo) {
    for (int i = 0; i < totalNumberOfSlots; i++) {
      if (occupiedSlots[i] != null && occupiedSlots[i].getRegNo().equalsIgnoreCase(regNo)) {
        return i;
      }
    }
    return -1;
  }

  public ArrayList<Integer> getSlotsOfColor(String color) {
    ArrayList<Integer> res = new ArrayList<>();
    for (int i = 0; i < totalNumberOfSlots; i++) {
      if (occupiedSlots[i] != null && occupiedSlots[i].getColor().equalsIgnoreCase(color)) {
        res.add(i);
      }
    }
    return res;
  }
}

public class Main {
  public static void main(String[] arguments) throws java.lang.Exception {
    Scanner sc = new Scanner(System.in);
    AutomatedCarParker automatedCarParker = new AutomatedCarParker();
    while (true) {
      String input = sc.nextLine();
      String args[] = input.split(" ");
      String operation = args[0];
      switch (operation) {
        case "create_parking_lot":
          automatedCarParker.createSlot(Integer.valueOf(args[1]));
          System.out.println("Created a parking slot with " + args[1] + " slots");
          break;
        case "park":
          String regNo = args[1];
          String color = args[2];
          Car car = new Car(regNo, color);
          int allotedSlot = automatedCarParker.allotSlot(car);
          if (allotedSlot == -1) {
            System.out.println("Sorry! parking lot is full");
            break;
          }
          System.out.println("Allocated slot number: " + (allotedSlot + 1));
          break;
        case "leave":
          int slot = Integer.valueOf(args[1]) - 1;
          automatedCarParker.updateOccupiedSlots(slot, null);
          System.out.println("Slot number " + (slot + 1) + " is free");
          break;
        case "status":
          automatedCarParker.displayStatus();
          break;
        case "registration_numbers_for_cars_with_colour":
          ArrayList<String> res = automatedCarParker.getCarsOfColor(args[1]);
          int resLen = res.size();
          for (int i = 0; i < resLen - 1; i++) {
            System.out.print(res.get(i) + ", ");
          }
          if (resLen > 0)
            System.out.println(res.get(resLen - 1));
          break;
        case "slot_number_for_registration_number":
          int resSlot = automatedCarParker.getSlotNumberOfCar(args[1]);
          if (resSlot == -1) {
            System.out.println("Not found");
            break;
          }
          System.out.println(resSlot + 1);
          break;
        case "slot_numbers_for_cars_with_colour":
          ArrayList<Integer> resSlots = automatedCarParker.getSlotsOfColor(args[1]);
          int resSlotsLen = resSlots.size();
          for (int i = 0; i < resSlotsLen - 1; i++) {
            System.out.print((resSlots.get(i) + 1) + ", ");
          }
          if (resSlotsLen > 0)
            System.out.println(resSlots.get(resSlotsLen - 1) + 1);
          break;
        default:
          System.out.println("command not found");
      }
    }
  }
}
