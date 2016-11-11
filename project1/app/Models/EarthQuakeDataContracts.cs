using System.Runtime.Serialization;

namespace EarthquakeViz.Models
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
        public int magnitude { get; set; }

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
}
