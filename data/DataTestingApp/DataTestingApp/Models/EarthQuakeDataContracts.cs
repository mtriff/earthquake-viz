using System.Runtime.Serialization;

namespace DataTestingApp
{
    [DataContract]
    public class DrillPostQuery
    {
        [DataMember]
        public string queryType { get; set; }

        [DataMember]
        public string query { get; set; }

    }

    [DataContract]
    public class MagnitudeTimeSet
    {
        [DataMember(Name = "QuakeDate", EmitDefaultValue = false)]
        public string quakeDate { get; set; }

        [DataMember(Name = "QuakeHour", EmitDefaultValue = false)]
        public string quakeHour { get; set; }

        [DataMember(Name = "Magnitude", EmitDefaultValue = false)]
        public float magnitude { get; set; }

        [DataMember(Name = "Amount", EmitDefaultValue = false)]
        public int amount { get; set; }
    }

    [DataContract]
    public class MagnitudeOverTime
    {
        [DataMember(Name = "columns", EmitDefaultValue = false)]
        public string[] columnNames { get; set; }

        [DataMember(Name = "rows", EmitDefaultValue = false)]
        public MagnitudeTimeSet[] magnitudeTimeSet { get; set; }
    }

    [DataContract]
    public class TsunamiTimeSet
    {
        [DataMember(Name = "QuakeDate", EmitDefaultValue = false)]
        public string quakeDate { get; set; }

        [DataMember(Name = "QuakeHour", EmitDefaultValue = false)]
        public string quakeHour { get; set; }

        [DataMember(Name = "Tsunami", EmitDefaultValue = false)]
        public float tsunami { get; set; }

        [DataMember(Name = "Amount", EmitDefaultValue = false)]
        public int amount { get; set; }
    }

    [DataContract]
    public class TsunamiOverTime
    {
        [DataMember(Name = "columns", EmitDefaultValue = false)]
        public string[] columnNames { get; set; }

        [DataMember(Name = "rows", EmitDefaultValue = false)]
        public TsunamiTimeSet[] tsunamiTimeSet { get; set; }
    }

    [DataContract]
    public class MagnitudeLatLongTimeSet
    {
        [DataMember(Name = "QuakeDate", EmitDefaultValue = false)]
        public string quakeDate { get; set; }

        [DataMember(Name = "QuakeHour", EmitDefaultValue = false)]
        public string quakeHour { get; set; }

        [DataMember(Name = "Latitude", EmitDefaultValue = false)]
        public float latitude { get; set; }

        [DataMember(Name = "Longitude", EmitDefaultValue = false)]
        public float longitude { get; set; }

        [DataMember(Name = "Magnitude", EmitDefaultValue = false)]
        public float magnitude { get; set; }
        
    }

    [DataContract]
    public class MagnitudeByLatLongOverTime
    {
        [DataMember(Name = "columns", EmitDefaultValue = false)]
        public string[] columnNames { get; set; }

        [DataMember(Name = "rows", EmitDefaultValue = false)]
        public MagnitudeLatLongTimeSet[] magnitudeLatLongTimeSet { get; set; }
    }
}
