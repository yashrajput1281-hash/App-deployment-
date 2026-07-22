package com.example.data.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AssignmentInd
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FamilyRestroom
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.HowToVote
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Train
import androidx.compose.ui.graphics.vector.ImageVector

data class CscServiceItem(
    val id: String,
    val title: String,
    val category: String,
    val description: String,
    val estimatedFee: String,
    val icon: ImageVector,
    val requiresDocs: String
)

object CscServiceCatalog {
    val services = listOf(
        CscServiceItem(
            id = "photocopy",
            title = "Photocopy & Printing",
            category = "Printing & Scanning",
            description = "High quality Black & White, Color printing, Photo printing, Scanning & Document Lamination",
            estimatedFee = "₹2 - ₹50",
            icon = Icons.Default.Print,
            requiresDocs = "Original or Digital copy of document"
        ),
        CscServiceItem(
            id = "aadhaar_update",
            title = "Aadhaar Card Update",
            category = "UIDAI Services",
            description = "Address change, Name correction, Phone number update, Biometric update & e-Aadhaar printout",
            estimatedFee = "₹50 - ₹100",
            icon = Icons.Default.Badge,
            requiresDocs = "Aadhaar number, Proof of Address/Identity"
        ),
        CscServiceItem(
            id = "railway_ticket",
            title = "Railway Ticket Booking",
            category = "Travel Services",
            description = "IRCTC Train ticket booking, Tatkal ticket booking, PNR status check & Train cancellation",
            estimatedFee = "₹30 + Govt Fare",
            icon = Icons.Default.Train,
            requiresDocs = "Passenger Name, Age, ID Details, Source & Destination"
        ),
        CscServiceItem(
            id = "pan_card",
            title = "PAN Card Application",
            category = "Taxation & Finance",
            description = "New NSDL/UTI PAN card, PAN correction/update, Instant e-PAN generation",
            estimatedFee = "₹107 - ₹150",
            icon = Icons.Default.CreditCard,
            requiresDocs = "Aadhaar card, Passport size photo, Mobile number"
        ),
        CscServiceItem(
            id = "family_id",
            title = "Parivar Pehchan Patra (PPP)",
            category = "CPLO & Govt Schemes",
            description = "Family ID creation, Correction in Family Income/Members, Haryana PPP updates",
            estimatedFee = "₹50 - ₹100",
            icon = Icons.Default.FamilyRestroom,
            requiresDocs = "All Family Aadhaar Cards, Bank Account, Electricity Bill"
        ),
        CscServiceItem(
            id = "certificates",
            title = "Caste / Residence Certificate",
            category = "Government Certificates",
            description = "Income certificate, OBC/SC/ST Caste certificate, Resident & Domicile certificates",
            estimatedFee = "₹50 - ₹100",
            icon = Icons.Default.AssignmentInd,
            requiresDocs = "Family ID, Self declaration, Sarpanch verification"
        ),
        CscServiceItem(
            id = "voter_card",
            title = "Voter ID Services",
            category = "Election Commission",
            description = "New Voter Registration (Form 6), Duplicate Voter Card, Address shift (Form 8)",
            estimatedFee = "₹30 - ₹50",
            icon = Icons.Default.HowToVote,
            requiresDocs = "Aadhaar card, Birth proof, Passport photo"
        ),
        CscServiceItem(
            id = "ayushman_card",
            title = "Ayushman Bharat Card",
            category = "Health Services",
            description = "Golden Card registration, ₹5 Lakh Health Insurance card download & printing",
            estimatedFee = "₹30 - ₹50",
            icon = Icons.Default.HealthAndSafety,
            requiresDocs = "Ration card or Family ID, Aadhaar card"
        ),
        CscServiceItem(
            id = "banking_aeps",
            title = "Banking & Micro ATM",
            category = "Banking Services",
            description = "Aadhaar Enabled Payment System (AEPS), Cash Withdrawal, Balance Enquiry, Money Transfer",
            estimatedFee = "Free - ₹20",
            icon = Icons.Default.AccountBalance,
            requiresDocs = "Aadhaar linked Bank Account & Biometric"
        ),
        CscServiceItem(
            id = "cplo_works",
            title = "CPLO Panchayat Works",
            category = "CPLO Works",
            description = "Land Jamabandi / Nakal, Electricity & Water Bill Payments, Old Age Pension status",
            estimatedFee = "Govt charges + ₹30",
            icon = Icons.Default.Description,
            requiresDocs = "Property Khewat no or Bill Consumer Account ID"
        ),
        CscServiceItem(
            id = "custom_service",
            title = "Other Custom Work",
            category = "General Assistance",
            description = "Any other CSC, online form filling, passport application, or digital work requirement",
            estimatedFee = "As per work",
            icon = Icons.Default.Build,
            requiresDocs = "Relevant documents as per requirement"
        )
    )
}
