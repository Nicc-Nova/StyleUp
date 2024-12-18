import axios from "axios";

/**
 * Creates a new room for the user.
 * @param {string} roomName - The name of the room to create.
 * @param {string} token - The user's authentication token.
 * @param {function} onSuccess - Callback to execute after successful room creation.
 * @param {function} onError - Callback to execute on error, receives an error message.
 */
export const createRoom = async (roomName, token, onSuccess, onError) => {
  try {
    const headers = {
      Authorization: `Bearer ${token}`,
    };

    console.log("Creating Room:", roomName);

    const response = await axios.post(
      "http://localhost:8080/api/room/create",
      { roomName },
      { headers }
    );

    if (response.status === 200) {
      console.log("Room created successfully:", response.data);
      if (onSuccess) onSuccess(response.data); // Call success callback
    }
  } catch (error) {
    let errorMessage = "Room creation failed: ";
    if (error.response) {
      errorMessage += error.response.data.message || "Unknown server error.";
    } else if (error.request) {
      errorMessage += "No response from server.";
    } else {
      errorMessage += error.message;
    }
    console.error(errorMessage);

    if (onError) onError(errorMessage); // Call error callback
  }
};