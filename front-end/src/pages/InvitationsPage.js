import BannerMenu from "../components/BannerMenu";
import { useNavigate } from "react-router-dom";
import { React, useContext, useEffect, useState } from "react";
import { AuthContext } from "../contexts/AuthContext";
import "../assets/stylesheets/MyRoomsPage.css"
import axios from 'axios';

function InvitationsPage() {
    
    const navigate = useNavigate();
    const { isLoggedIn, token } = useContext(AuthContext);
    const [invitations, setInvitations] = useState([]);

    const [errorMessage, setErrorMessage] = useState('');
    const [sent, setSent] = useState([]);
    const [loading, setLoading] = useState(true); // Loading state to manage async data fetching
    const [loading1, setLoading1] = useState(true); // Loading state to manage async data fetching

    useEffect(() => {
        if (!isLoggedIn) {
          navigate("/Login"); // Redirect to Login if not logged in
        } else {
            getInvitations(); //get received invitations
            getSent();//get sent invitations
        }
    }, [isLoggedIn, navigate]); // Depend on isLoggedIn to trigger re-navigation
    
    useEffect(() => {
        console.log("Updated invitations:", invitations); // Observe state changes !for debugging!
    }, [invitations])
    

    const getInvitations = async () => {
        try {
          setLoading(true); // Start loading
    
          const headers = {
            Authorization: `Bearer ${token}`,
          };
    
          const invResponse = await axios.get("http://localhost:8080/api/invitation/getReceived", {headers})
    
          console.log("Invitation data:", invResponse.data) // Check invites 
          setInvitations(invResponse.data)
          //console.log("invitations:", invitations)
        } catch (error) {
          console.error("Failed to fetch user details:", error);
          setErrorMessage(error);
        } finally {
          setLoading(false) // Stop loading after fetch is done
        }
    }

      const getSent = async () => {
        try {
          setLoading1(true); // Start loading
    
          const headers = {
            Authorization: `Bearer ${token}`,
          };
    
          const sentResponse = await axios.get("http://localhost:8080/api/invitation/getSent", {headers})
    
          console.log("Invitation data:", sentResponse.data); // Check invites 
          setSent(sentResponse.data)
    
        } catch (error) {
          console.error("Failed to fetch user details:", error);
          setErrorMessage(error);
        } finally {
          setLoading1(false); // Stop loading after fetch is done
        }
        
    }

    const rejectInvitation = async (invitation) => {
        try {    
          const headers = {
            Authorization: `Bearer ${token}`,
          };
    
          const sentResponse = await axios.post("http://localhost:8080/api/invitation/reject", invitation,{headers})
    
          console.log("Invitation data:", sentResponse.data); // Check invites 
          if (sentResponse.status === 200) {
            console.log('Invitation deleted successfully:', sentResponse.data);
            // Update the local state to remove the deleted decoration
            setInvitations((prevInvitations) => ({
                ...prevInvitations,
                invitations: prevInvitations.filter((inv) => inv.invitation_id !== invitation.invitation_id),
            }));
            setSent((prevSent) => ({
              ...prevSent,
              sent: prevSent.filter((inv) => inv.invitation_id !== invitation.invitation_id),
          }));
          }
    
        } catch (error) {
          console.error("Failed to reject invitation:", error);
          setErrorMessage(error);
        }         
    }

    const acceptInvitation = async (invitation) => {
        try {    
          const headers = {
            Authorization: `Bearer ${token}`,
          };
    
          const sentResponse = await axios.post("http://localhost:8080/api/invitation/accept", invitation, {headers})
    
          if (sentResponse.status === 200) {
            console.log('Collab deleted successfully:', sentResponse.data);
            setInvitations((prevInvitations) => ({
                ...prevInvitations,
                invitations: prevInvitations.filter((invite) => invite.invitation_id !== invitation.invitations_id),
            }));
        }
    
        } catch (error) {
          console.error("Failed to accept invitation:", error);
          setErrorMessage(error);
        }         
    }

    const renderContent = () => {
        if (loading) {
          return <p style={{ textAlign: 'center' }}>Loading invitations...</p>;
        }
        console.log("invitation length: ", invitations.length);
        if (invitations.length > 0) {
            return (
                <div className="rooms-list">
                    {Array.isArray(invitations) &&invitations.map((invitation) => (
                        <div className="room-item" key={invitation.id}>
                            <h2>{invitation.roomName}</h2>
                            <p><strong>Invited By:</strong> {invitation.senderName}</p>
                            <div className="invitation-actions">
                                <button
                                    onClick={() => acceptInvitation(invitation)}
                                    style={{
                                        backgroundColor: "#633B48",
                                        color: "#F1E8E8",
                                        border: "2px solid #4B2C37",
                                        borderRadius: "5px",
                                        padding: "10px 15px",
                                        cursor: "pointer",
                                        margin: "5px",
                                        fontSize: "14px",
                                        transition: "background-color 0.3s",
                                    }}
                                    onMouseOver={(e) => (e.target.style.backgroundColor = "#4B2C37")}
                                    onMouseOut={(e) => (e.target.style.backgroundColor = "#633B48")}
                                    /**for accessibility*/
                                    onFocus={(e) => (e.target.style.backgroundColor = "#4B2C37")}
                                    onBlur={(e) => (e.target.style.backgroundColor = "#633B48")}
                                >
                                    Accept
                                </button>
                                <button
                                    onClick={() => rejectInvitation(invitation)}
                                    style={{
                                        backgroundColor: "#F1E8E8",
                                        color: "#633B48",
                                        border: "2px solid #633B48",
                                        borderRadius: "5px",
                                        padding: "10px 15px",
                                        cursor: "pointer",
                                        margin: "5px",
                                        fontSize: "14px",
                                        transition: "background-color 0.3s, color 0.3s",
                                    }}
                                    onMouseOver={(e) => {
                                        e.target.style.backgroundColor = "#633B48";
                                        e.target.style.color = "#F1E8E8";

                                    }}
                                    onMouseOut={(e) => {
                                        e.target.style.backgroundColor = "#F1E8E8";
                                        e.target.style.color = "#633B48";
            
                                    }}
                                    onFocus={(e) => {
                                        //for accessibility
                                        e.target.style.backgroundColor = "#633B48";
                                        e.target.style.color = "#F1E8E8";
                                    }}
                                    onBlur={(e) => {
                                        //for accessibility
                                        e.target.style.backgroundColor = "#F1E8E8";
                                        e.target.style.color = "#633B48";
                                    }}

                                >
                                    Decline
                                </button>
                            </div>
                        </div>
                    ))}
                    {errorMessage && <p style={{ textAlign: "center", color: 'red' }}>{errorMessage}</p>}
                </div>
          );
        } 
        // Memoize the value to prevent unnecessary re-renders
        return (
            <div className="no-room-message">
              <h2>You don't have any invitations yet...</h2>
            </div>
        );
      };

      const renderSent = () => {
        if (loading1) {
          return <p style={{ textAlign: 'center' }}>Loading sent invitations...</p>;
        }
    
        if (sent.length > 0) {
            return (
                <div className="rooms-list">
                    {Array.isArray(sent) && sent.map((send) => (
                        <div className="room-item" key={send.id}>
                            <h2>{send.roomName}</h2>
                            <p><strong>Sent to: </strong> {send.email}</p>
                            <div className="invitation-actions">
                                <button
                                    onClick={() => rejectInvitation(send)}
                                    style={{
                                        backgroundColor: "#F1E8E8",
                                        color: "#633B48",
                                        border: "2px solid #633B48",
                                        borderRadius: "5px",
                                        padding: "10px 15px",
                                        cursor: "pointer",
                                        margin: "5px",
                                        fontSize: "14px",
                                        transition: "background-color 0.3s, color 0.3s",
                                    }}
                                    onMouseOver={(e) => {
                                        e.target.style.backgroundColor = "#633B48";
                                        e.target.style.color = "#F1E8E8";
                                    }}
                                    onMouseOut={(e) => {
                                        e.target.style.backgroundColor = "#F1E8E8";
                                        e.target.style.color = "#633B48";
                                    }}
                                >
                                    Withdraw
                                </button>
                            </div>
                        </div>
                    ))}
                    {errorMessage && <p style={{ textAlign: "center", color: 'red' }}>{errorMessage}</p>}
                </div>
          );
        } 
        // Memoize the value to prevent unnecessary re-renders
        return (
          <>
            <div className="no-room-message">
              <h2>You haven't sent any invitations yet...</h2>
            </div>
          </>
        );
      };


    //We are gonna have blank login credentials until we set up the backend
    return (
        <div className="container">
            <BannerMenu/>
            {isLoggedIn ? (
            <div className='main-content'>
                <h1>Received Invitations:</h1>
                {renderContent()}
                <h1>Pending Invitations:</h1>
                {renderSent()}
            </div>
            ):(
                <p style={{ textAlign: 'center' }}>Redirecting to login...</p>
            )}
        </div>
    )
}
export default InvitationsPage;