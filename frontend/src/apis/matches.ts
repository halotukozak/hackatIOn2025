import { base_url } from "./base.ts";

export const swipeAck = async (
  userId: number,
  swipedUserId: number
): Promise<string> => {
  return sendSwipe(userId, swipedUserId, "ACK");
};

export const swipeNack = async (
  userId: number,
  swipedUserId: number
): Promise<string> => {
  return sendSwipe(userId, swipedUserId, "NACK");
};

const sendSwipe = async (
  userId: number,
  swipedUserId: number,
  status: "ACK" | "NACK"
): Promise<string> => {
  try {
    const res = await fetch(
      base_url() + `/matches/swipe/${userId}/${swipedUserId}?status=${status}`,
      {
        method: "POST",
        headers: { "Content-Type": "application/json" },
      }
    );

    if (!res.ok) {
      const errorMsg = await res.text();
      throw new Error(`Swipe failed: ${errorMsg}`);
    }

    const result = await res.text(); // response is a string: "ACK" | "NACK" | "NONE"
    return result;
  } catch (err) {
    console.error("Error during swipe:", err);
    throw err;
  }
};
